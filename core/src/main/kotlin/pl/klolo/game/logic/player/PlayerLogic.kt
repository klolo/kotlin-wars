package pl.klolo.game.logic.player

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.PolygonShape
import pl.klolo.game.configuration.Colors
import pl.klolo.game.configuration.Colors.blueLight
import pl.klolo.game.engine.*
import pl.klolo.game.event.*
import pl.klolo.game.common.executeAfterDelay
import pl.klolo.game.entity.*
import pl.klolo.game.logic.BulletLogic
import pl.klolo.game.logic.helper.ExplosionLights
import pl.klolo.game.logic.helper.PopupMessages
import pl.klolo.game.physics.GamePhysics

const val bonusLifetime = 15f

class PlayerLogic(
        private val profileHolder: ProfileHolder,
        private val highscore: Highscore,
        private val gamePhysics: GamePhysics,
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {

    private var explosionLights = ExplosionLights(gameLighting, 50f)
    private var popupMessages = PopupMessages(entityRegistry, eventProcessor)
    private var moveLogic = getMoveLogicImplementation(profileHolder.activeProfile, eventProcessor)
    private lateinit var engineFire: ParticleEntity

    private var hasShield = false

    private var lifeLevel = 100
    private var points = 0
    private val defaultBulletPower = 10
    private var enabledSuperBulletCounter = 0
    private var bulletPower = defaultBulletPower
    private var doublePoints = false
    private var isImmortal = false

    private lateinit var physicsShape: PolygonShape
    private lateinit var body: Body
    private lateinit var playerLight: PointLight
    private lateinit var laserConfiguration: EntityConfiguration

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        physicsShape.dispose()
        explosionLights.onDispose()
        gamePhysics.destroy(body)
    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        Gdx.app.debug(this.javaClass.name, "createSubscription")
        y = getPlayerBottomMargin(profileHolder.activeProfile, height)

        playerLight = gameLighting.createPointLight(100, blueLight, 50f, x, y)
        laserConfiguration = entityRegistry.getConfigurationById("laserBlue01")

        moveLogic.initialize(this)
        moveLogic.createSubscription(this)
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
                    eventProcessor.sendEvent(PlaySound(SoundEffect.FOUND_BONUS))
                    enableSuperBullet()
                    executeAfterDelay(bonusLifetime) { disableSuperBullet() }
                }
                .onEvent(EnableShield) {
                    eventProcessor.sendEvent(PlaySound(SoundEffect.FOUND_BONUS))
                    hasShield = true
                    executeAfterDelay(bonusLifetime) {
                        hasShield = false
                        eventProcessor.sendEvent(DisableShield)
                    }
                }
                .onEvent(EnableDoublePoints) {
                    eventProcessor.sendEvent(PlaySound(SoundEffect.FOUND_BONUS))
                    doublePoints = true
                    popupMessages.show(this, "x2")
                    executeAfterDelay(bonusLifetime) {
                        doublePoints = false
                        popupMessages.show(this, "x1")
                    }
                }

        createPhysics()
        createEngineFire()
    }

    private fun createEngineFire() {
        val engineFireConfiguration = entityRegistry.getConfigurationById("engineFire")
        engineFire = createEntity(engineFireConfiguration)
        eventProcessor.sendEvent(RegisterEntity(engineFire))
    }

    private fun addPoints(it: AddPoints) {
        points = when (doublePoints) {
            true -> points + (it.points * 2)
            false -> points + it.points
        }
    }

    private fun SpriteEntityWithLogic.onAddPlayerLife(it: AddPlayerLife) {
        Gdx.app.debug(this.javaClass.name, "increase life level $it")
        lifeLevel += it.lifeAmount
        if (lifeLevel > 100) {
            lifeLevel = 100
        }
        else {
            eventProcessor.sendEvent(PlaySound(SoundEffect.YIPEE))
        }

        eventProcessor.sendEvent(ChangePlayerLfeLevel(lifeLevel))
        popupMessages.show(this, "+${it.lifeAmount}%")
    }

    private fun enableSuperBullet() {
        Gdx.app.debug(this.javaClass.name, "Enable super bullet. enabled: ${enabledSuperBulletCounter}, current power: ${bulletPower}")
        laserConfiguration = entityRegistry.getConfigurationById("laserBlue02")
        bulletPower *= 4
        enabledSuperBulletCounter++
        playerLight.distance = 150f
        playerLight.color = Colors.gold
    }

    private fun disableSuperBullet() {
        enabledSuperBulletCounter--
        if (enabledSuperBulletCounter == 0) {
            Gdx.app.debug(this.javaClass.name, "Disable super bullet.")
            laserConfiguration = entityRegistry.getConfigurationById("laserBlue01")
            bulletPower = defaultBulletPower
            playerLight.distance = 70f
            playerLight.color = blueLight
        }
    }

    private fun SpriteEntityWithLogic.onCollision(it: OnCollision) {
        val collidedEntity = it.entity!!
        if (isEnemyLaser(collidedEntity) && !hasShield && !isImmortal) {
            eventProcessor.sendEvent(PlaySound(SoundEffect.PLAYER_COLLISION))

            lifeLevel -= 10
            popupMessages.show(this, "-10%")

            executeAfterDelay(0.2f) {
                isImmortal = false
            }

            explosionLights.addLight(this)
            eventProcessor.sendEvent(ChangePlayerLfeLevel(lifeLevel))
            isImmortal = true

            if (lifeLevel <= 0) {
                onGameOver()
            }
        }
    }

    private fun SpriteEntityWithLogic.onGameOver() {
        highscore.setLastScore(points)

        eventProcessor.sendEvent(StopMusic)
        eventProcessor.sendEvent(PlaySound(SoundEffect.DESTROY_PLAYER))

        playerLight.distance *= 200
        display = false

        executeAfterDelay(0.15f) {
            eventProcessor.sendEvent(GameOver(lifeLevel))
        }
    }

    private fun SpriteEntityWithLogic.shootOnPosition() {
        val bulletXPosition = x + width / 2
        val bulletYPosition = y + height / 2

        val bulletEntity: SpriteEntityWithLogic = createEntity(laserConfiguration) {
            x = bulletXPosition
            y = bulletYPosition
        }

        (bulletEntity.logic as BulletLogic).isEnemyBullet = false
        bulletEntity.logic.bulletPower = bulletPower

        if (enabledSuperBulletCounter > 0) {
            bulletEntity.logic.apply {
                bulletLight.color = Colors.gold
                bulletLight.distance = 2f * bulletLight.distance
            }
        }

        eventProcessor.sendEvent(RegisterEntity(bulletEntity))
        eventProcessor.sendEvent(PlaySound(SoundEffect.PLAYER_SHOOT))
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        popupMessages.updatePosition(this)
        playerLight.setPosition(x + width / 2, y + height / 2)
        body.setTransform(x + width / 2, y + height / 2, 0.0f)
        moveLogic.onUpdate(this, it)
        updateEngineFirePosition()
    }

    private fun SpriteEntityWithLogic.updateEngineFirePosition() {
        val currentX = x
        val currentY = y
        val playerWidth = width
        engineFire.apply {
            this.effect.setPosition(currentX + playerWidth / 2, currentY)
        }
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        physicsShape = PolygonShape()
        physicsShape.setAsBox(width / 2, height / 2);
        body = gamePhysics.createDynamicBody()

        body.createFixture(gamePhysics.getStandardFixtureDef(physicsShape)).userData = this
    }
}