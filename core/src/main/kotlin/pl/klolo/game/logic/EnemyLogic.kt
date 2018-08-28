package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import pl.klolo.game.configuration.Colors
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.engine.isPlayerLaser
import pl.klolo.game.entity.*
import pl.klolo.game.event.*
import pl.klolo.game.extensions.addForeverSequence
import pl.klolo.game.extensions.addSequence
import pl.klolo.game.extensions.execute
import pl.klolo.game.logic.helper.ExplosionLights
import pl.klolo.game.logic.helper.PopupMessages
import pl.klolo.game.physics.GamePhysics

class EnemyLogic(
        private val entityRegistry: EntityRegistry,
        private val gamePhysics: GamePhysics,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {
    private var explosionLights = ExplosionLights(gameLighting, 50f)
    private var popupMessages: PopupMessages = PopupMessages(entityRegistry, eventProcessor)

    private lateinit var light: PointLight
    private lateinit var physicsShape: CircleShape
    private lateinit var body: Body
    private var life: Int = 0
    private val lifeFactory = 20
    var shootDelay = 3f

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        if (display) {
            light.remove()
            physicsShape.dispose()
            gamePhysics.destroy(body)
        }
    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        light = gameLighting.createPointLight(50, Colors.redLight, 40f, x, y)

        life = uniqueName
                .elementAt(uniqueName.lastIndex)
                .toString()
                .toInt() * lifeFactory

        createPhysics()

        addSequence(
                moveTo(x, y - Gdx.graphics.width - 100, 20f),
                execute { onDestroy() }
        )

        addForeverSequence(
                delay(shootDelay),
                execute {
                    val laserConfiguration = entityRegistry.getConfigurationById("laserRed01")
                    shootOnPosition(laserConfiguration)
                }
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
        else {
            explosionLights.addLight(this)
        }
    }

    private fun SpriteEntityWithLogic.onDestroyEnemy() {
        popupMessages.showAndRun(this, "+${height.toInt()}") {
            onDestroy()
        }

        onDispose()
        display = false
        eventProcessor.sendEvent(AddPoints(height.toInt()))
    }

    private fun SpriteEntityWithLogic.onDestroy() {
        if (shouldBeRemove) {
            return
        }
        shouldBeRemove = true
        eventProcessor.sendEvent(EnemyDestroyed)
        explosionLights.onDispose()
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

}