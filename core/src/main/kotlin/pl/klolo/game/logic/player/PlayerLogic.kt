package pl.klolo.game.logic.player

import box2dLight.PointLight
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.PolygonShape
import pl.klolo.game.configuration.Colors.blueLight
import pl.klolo.game.engine.*
import pl.klolo.game.entity.*
import pl.klolo.game.event.*
import pl.klolo.game.extensions.executeAfterDelay
import pl.klolo.game.logic.BulletLogic
import pl.klolo.game.logic.EntityLogic
import pl.klolo.game.logic.helper.ExplosionLights
import pl.klolo.game.logic.helper.PopupMessages
import pl.klolo.game.physics.GamePhysics

val doublePointsTime = 15f

class PlayerLogic(
        private val highscore: Highscore,
        private val gamePhysics: GamePhysics,
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic>, PlayerMoveLogic(eventProcessor) {

    private var explosionLights = ExplosionLights(gameLighting, 50f)
    private var popupMessages = PopupMessages(entityRegistry, eventProcessor)
    private var hasShield = false

    var lifeLevel = 100
    var points = 0
    val defaulBulletPower = 10
    var enabledSuperBulletCounter = 0
    var bulletPower = defaulBulletPower
    var doublePoints = false

    lateinit var physicsShape: PolygonShape
    lateinit var body: Body
    lateinit var playerLight: PointLight
    lateinit var laserConfiguration: EntityConfiguration

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        physicsShape.dispose()
        playerLight.remove()
        explosionLights.onDispose()
        gamePhysics.destroy(body)
    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        println("PlayerLogic creating...")

        playerLight = gameLighting.createPointLight(100, blueLight, 50f, x, y)
        laserConfiguration = entityRegistry.getConfigurationById("laserBlue01")

        initializeMoving()
                .onEvent(OnCollision::class.java) {
                    onCollision(it)
                }
                .onEvent(OnSpace) {
                    shootOnPosition()
                }
                .onEvent(AddPoints::class.java) {
                    addPoints(it)
                }
                .onEvent(AddPlayerLife::class.java) {
                    onAddPlayerLife(it)
                }
                .onEvent(EnableSuperBullet) {
                    enableSuperBullet()
                    executeAfterDelay(15f) { disableSuperBullet() }
                }
                .onEvent(EnableShield) {
                    hasShield = true
                    executeAfterDelay(15f) {
                        hasShield = false
                        eventProcessor.sendEvent(DisableShield)
                    }
                }
                .onEvent(EnableDoublePoints) {
                    doublePoints = true
                    popupMessages.show(this, "x2")
                    executeAfterDelay(doublePointsTime) {
                        doublePoints = false
                        popupMessages.show(this, "x1")
                    }
                }

        createPhysics()
    }

    private fun addPoints(it: AddPoints) {
        points = when (doublePoints) {
            true -> points + (it.points * 2)
            false -> points + it.points
        }
    }

    private fun SpriteEntityWithLogic.onAddPlayerLife(it: AddPlayerLife) {
        println("increase life level $it")
        lifeLevel += it.lifeAmount
        if (lifeLevel > 100) {
            lifeLevel = 100
        }

        eventProcessor.sendEvent(ChangePlayerLfeLevel(lifeLevel))
        popupMessages.show(this, "+${it.lifeAmount}%")
    }

    private fun enableSuperBullet() {
        println("Enable super bullet. enabled: ${enabledSuperBulletCounter}, current power: ${bulletPower}")
        laserConfiguration = entityRegistry.getConfigurationById("laserBlue02")
        bulletPower *= 2
        enabledSuperBulletCounter++
        playerLight.distance = 100f
    }

    private fun disableSuperBullet() {
        enabledSuperBulletCounter--
        if (enabledSuperBulletCounter == 0) {
            println("Disable super bullet.")
            laserConfiguration = entityRegistry.getConfigurationById("laserBlue01")
            bulletPower = defaulBulletPower
            playerLight.distance = 70f
        }
    }

    private fun SpriteEntityWithLogic.onCollision(it: OnCollision) {
        val collidedEntity = it.entity!!
        if (isEnemyLaser(collidedEntity)) {
            if (hasShield) {
                return
            }

            lifeLevel -= 10
            popupMessages.show(this, "-10%")

            explosionLights.addLight(this)
            eventProcessor.sendEvent(ChangePlayerLfeLevel(lifeLevel))

            if (lifeLevel <= 0) {
                highscore.setLastScore(points)
                eventProcessor.sendEvent(GameOver(lifeLevel))
            }
        }
    }

    private fun SpriteEntityWithLogic.shootOnPosition() {
        val bulletXPosition = x + width / 2
        val bulletYPosition = y + height / 2

        val bulletEntity: SpriteEntityWithLogic = createEntity(laserConfiguration, applicationContext) {
            x = bulletXPosition
            y = bulletYPosition
        }

        (bulletEntity.logic as BulletLogic).isEnemyBullet = false
        bulletEntity.logic.bulletPower = bulletPower

        eventProcessor.sendEvent(RegisterEntity(bulletEntity))
        eventProcessor.sendEvent(PlaySound(SoundEffect.PLAYER_SHOOT))
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        popupMessages.updatePosition(this)
        playerLight.setPosition(x + width / 2, y + height / 2)
        body.setTransform(x + width / 2, y + height / 2, 0.0f)
        checkPosition()
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        physicsShape = PolygonShape()
        physicsShape.setAsBox(width / 2, height / 2);
        body = gamePhysics.createDynamicBody()

        body.createFixture(gamePhysics.getStandardFixtureDef(physicsShape)).userData = this
    }
}