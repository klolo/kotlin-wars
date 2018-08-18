package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import pl.klolo.game.GameLighting
import pl.klolo.game.applicationContext
import pl.klolo.game.entity.*
import pl.klolo.game.physics.GamePhysics
import pl.klolo.game.event.*
import pl.klolo.game.extensions.execute

class EnemyLogic(
        private val entityRegistry: EntityRegistry,
        private val gamePhysics: GamePhysics,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {
    private var light: PointLight? = null
    private var physicsShape: CircleShape? = null
    private lateinit var body: Body
    private var life = 100

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        light?.remove()
        physicsShape?.dispose()
        gamePhysics.destroy(body)
    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        light = gameLighting.createPointLight(50, "#ff1111", 100f, x, y - height / 2)
        createPhysics()

        addAction(sequence(
                moveTo(x, y - Gdx.graphics.width - 100, 20f),
                execute(Runnable { onDestroy() })
        ))

        addAction(
                forever(sequence(
                        delay(1f),
                        execute(Runnable {
                            val laserConfiguration = entityRegistry.getConfigurationById("laserRed01")
                            if (laserConfiguration != null) {
                                shootOnPosition(laserConfiguration)
                            }
                        })
                ))
        )

        eventProcessor.subscribe(id)
                .onEvent(OnCollision::class.java) {
                    val collidedEntity = it.entity
                    if (collidedEntity != null && collidedEntity.uniqueName.contains("laserBlue")) {
                        onCollisionWithLaser(collidedEntity)
                    }
                }
    }

    private fun SpriteEntityWithLogic.shootOnPosition(laserConfiguration: EntityConfiguration) {
        val bulletXPosition = x + width / 2
        val bulletYPosition = y - height

        val bulletEntity: SpriteEntityWithLogic = createEntity(laserConfiguration, applicationContext, false) {
            x = bulletXPosition
            y = bulletYPosition
        } as SpriteEntityWithLogic

        val bulletLogic = bulletEntity.logic as BulletLogic
        bulletLogic.direction = Direction.DOWN
        bulletLogic.lightColor = "#ddeecc"
        bulletLogic.apply {
            initialize.invoke(bulletEntity)

        }
        eventProcessor.sendEvent(RegisterEntity(bulletEntity))
    }

    private fun SpriteEntityWithLogic.onCollisionWithLaser(collidedEntity: Entity) {
        val damageLevel = collidedEntity.uniqueName
                .elementAt(collidedEntity.uniqueName.lastIndex)
                .toString().toInt()
        life -= damageLevel * 10

        if (life < 0) {
            onDestroyEnemy()
        }
    }

    fun SpriteEntityWithLogic.onDestroyEnemy() {
        // Size enemy is points to get
        eventProcessor.sendEvent(AddPoints(height.toInt()))
        onDestroy()
    }

    fun SpriteEntityWithLogic.onDestroy() {
        shouldBeRemove = true
        eventProcessor.sendEvent(EnemyDestroyed)
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        light?.setPosition(x + width / 2, y + height)
        body.setTransform(x + width / 2, y + height / 2, 0.0f)
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        val bodyDef = BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
        }

        physicsShape = CircleShape().apply { radius = width / 2 }

        val fixtureDef = FixtureDef().apply {
            shape = physicsShape
            density = 0.5f
            friction = 0.4f
            restitution = 0.6f
        }

        body = gamePhysics.createBody(bodyDef)
        val fixture = body.createFixture(fixtureDef)
        fixture?.userData = this
    }

}