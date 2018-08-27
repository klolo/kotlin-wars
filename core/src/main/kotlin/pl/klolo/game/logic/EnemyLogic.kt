package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.configuration.Colors
import pl.klolo.game.engine.isPlayerLaser
import pl.klolo.game.entity.*
import pl.klolo.game.event.*
import pl.klolo.game.extensions.addSequence
import pl.klolo.game.extensions.execute
import pl.klolo.game.extensions.executeAfterDelay
import pl.klolo.game.physics.GamePhysics
import java.util.*

class EnemyLogic(
        private val entityRegistry: EntityRegistry,
        private val gamePhysics: GamePhysics,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {
    private var resultLabel: TextEntity? = null
    private lateinit var light: PointLight
    private lateinit var physicsShape: CircleShape
    private lateinit var body: Body
    private var life: Int = 0
    private val lifeFactory = 20
    var shootDelay = 3f

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {

    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        light = gameLighting.createPointLight(50, Colors.redLight, 40f, x, y)

        life = uniqueName
                .elementAt(uniqueName.lastIndex)
                .toString()
                .toInt() * lifeFactory

        createPhysics()

        addAction(
                sequence(
                        moveTo(x, y - Gdx.graphics.width - 100, 20f),
                        execute { onDestroy() }
                ))

        addAction(
                forever(sequence(
                        delay(shootDelay),
                        execute {
                            val laserConfiguration = entityRegistry.getConfigurationById("laserRed01")
                            shootOnPosition(laserConfiguration)
                        }
                ))
        )

        eventProcessor.subscribe(id)
                .onEvent(OnCollision::class.java) {
                    val collidedEntity = it.entity!!
                    if (isPlayerLaser(collidedEntity) && display) {
                        onCollisionWithLaser(collidedEntity as SpriteEntityWithLogic)
                    }
                }
    }

    private fun SpriteEntityWithLogic.shootOnPosition(laserConfiguration: EntityConfiguration) {
        val bulletXPosition = x + width / 2 // width of the enemy
        val offset = 20
        val bulletYPosition = y - height - offset

        val bulletEntity: SpriteEntityWithLogic = createEntity(laserConfiguration, applicationContext, false) {
            x = bulletXPosition - width / 2 // width of the bullet
            y = bulletYPosition

        } as SpriteEntityWithLogic

        val bulletLogic = bulletEntity.logic as BulletLogic
        bulletLogic.direction = Direction.DOWN
        bulletLogic.lightColor = Colors.redLightAccent
        bulletLogic.apply {
            initialize.invoke(bulletEntity)
        }
        eventProcessor.sendEvent(RegisterEntity(bulletEntity))
    }

    private fun SpriteEntityWithLogic.onCollisionWithLaser(collidedEntity: SpriteEntityWithLogic) {
        life -= (collidedEntity.logic as BulletLogic).bulletPower
        if (life <= 0) {
            onDestroyEnemy()
        }
    }

    fun SpriteEntityWithLogic.onDestroyEnemy() {
        resultLabel = createResultLabel("+${height.toInt()}")
        resultLabel?.setPosition(x, y)
        resultLabel?.apply {
            addSequence(
                    alpha(0f, 1f),
                    execute { onDestroy() }
            )
        }

        display = false
        light.remove()
        physicsShape.dispose()
        gamePhysics.destroy(body)

        eventProcessor.sendEvent(AddPoints(height.toInt()))
    }

    fun SpriteEntityWithLogic.onDestroy() {
        if (shouldBeRemove) {
            return
        }
        shouldBeRemove = true
        eventProcessor.sendEvent(EnemyDestroyed)
        resultLabel?.shouldBeRemove = true
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        light.setPosition(x + width / 2, y + height / 2)
        body.setTransform(x + width / 2, y + height / 2, 0.0f)

        val downMarginBeforeDestroyEntity = 20
        if (x + downMarginBeforeDestroyEntity < 0) {
            shouldBeRemove = true
        }
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        body = gamePhysics.createDynamicBody()
        physicsShape = CircleShape().apply { radius = width / 2 }
        val fixture = body.createFixture(gamePhysics.getStandardFixtureDef(physicsShape))
        fixture?.userData = this
    }

    private fun createResultLabel(labelText: String): TextEntity {
        return createEntity<TextEntity>(entityRegistry.getConfigurationById("text"), applicationContext)
                .apply {
                    text = labelText
                    eventProcessor.sendEvent(RegisterEntity(this))
                    intializeFont()
                }
    }

}