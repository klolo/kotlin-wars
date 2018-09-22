package pl.klolo.game.logic.enemy

import box2dLight.PointLight
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import pl.klolo.game.common.addForeverSequence
import pl.klolo.game.common.addSequence
import pl.klolo.game.common.execute
import pl.klolo.game.configuration.Colors
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.engine.isPlayerLaser
import pl.klolo.game.entity.*
import pl.klolo.game.event.*
import pl.klolo.game.logic.BulletLogic
import pl.klolo.game.logic.Direction
import pl.klolo.game.logic.helper.PopupMessageConfiguration
import pl.klolo.game.logic.helper.PopupMessages
import pl.klolo.game.physics.GamePhysics


class EnemyLogic(
        private val entityRegistry: EntityRegistry,
        private val gamePhysics: GamePhysics,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {

    private val explosionConfiguration = entityRegistry.getConfigurationById("explosion")
    private var explosionLights = ExplosionEffect(gameLighting, 50f)
    private var popupMessages: PopupMessages = PopupMessages(entityRegistry, eventProcessor)

    private var explosion: ParticleEntity? = null
    private var light: PointLight? = null

    private lateinit var physicsShape: CircleShape
    private lateinit var body: Body
    private var life: Int = 0
    private var doublePoints = false
    private val lifeFactory = 20

    var shootDelay = 3f
    var speed = 1f
    private val lightDistance = 40f
    private val lightDistanceDistanceAfterExplosion = 400f

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        if (display) {
            physicsShape.dispose()
            gamePhysics.destroy(body)
        }
    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        val laserConfiguration = entityRegistry.getConfigurationById("laserRed01")
        light = gameLighting.createPointLight(50, Colors.redLight, lightDistance, x, y)

        life = uniqueName
                .elementAt(uniqueName.lastIndex)
                .toString()
                .toInt() * lifeFactory

        createPhysics()

        addSequence(
                moveTo(x, -1 * height, speed),
                execute { onDestroy() }
        )

        addForeverSequence(
                delay(shootDelay),
                execute { shootOnPosition(laserConfiguration) }
        )

        eventProcessor
                .subscribe(id)
                .onEvent(OnCollision::class) {
                    val collidedEntity = it.entity!!
                    if (isPlayerLaser(collidedEntity) && display) {
                        onCollisionWithLaser(collidedEntity as SpriteEntityWithLogic)
                    }
                }
                .onEvent(EnableDoublePoints) {
                    doublePoints = true
                }
                .onEvent(DisableDoublePoints) {
                    doublePoints = false
                }
    }

    private fun SpriteEntityWithLogic.shootOnPosition(laserConfiguration: EntityConfiguration) {
        val bulletXPosition = x + width / 2 // width of the enemy
        val offset = 20
        val bulletYPosition = y - height - offset

        val bulletEntity: SpriteEntityWithLogic = createEntity(laserConfiguration, false) {
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
        clearActions()
        showExplosion()
        showPopup()

        onDispose()
        display = false
        eventProcessor.sendEvent(AddPoints(height.toInt()))
    }

    private fun SpriteEntityWithLogic.showPopup() {
        val popupMessageConfiguration = PopupMessageConfiguration(
                message = "+${if (doublePoints) height.toInt() * 2 else height.toInt()}",
                callback = {
                    onDestroy()
                }
        )
        popupMessages.show(this, popupMessageConfiguration)
    }

    private fun SpriteEntityWithLogic.showExplosion() {
        explosion = createEntity(explosionConfiguration)
        val currentX = x
        val currentY = y
        explosion.apply {
            this!!.effect.setPosition(currentX, currentY)
        }
        eventProcessor.sendEvent(RegisterEntity(explosion))
        light?.color = Colors.redLight
        val explosionLightFadeOutTime = 0.25f
        addAction(fadeOut(explosionLightFadeOutTime))
    }

    private fun SpriteEntityWithLogic.onDestroy() {
        if (shouldBeRemove) {
            return
        }
        shouldBeRemove = true
        eventProcessor.sendEvent(EnemyDestroyed)
        light?.remove()
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        light?.setPosition(x + width / 2, y + height / 2)
        body.setTransform(x + width / 2, y + height / 2, 0.0f)
        explosionLights.updateLight()

        if (!display) {
            updateLightAfterDestroyedEnemy()
        }

        updateExplosion()

        val downMarginBeforeDestroyEntity = 20
        if (x + downMarginBeforeDestroyEntity < 0) {
            shouldBeRemove = true
        }
    }

    private fun SpriteEntityWithLogic.updateLightAfterDestroyedEnemy() {
        light?.distance = lightDistanceDistanceAfterExplosion * color.a
        if ((light?.distance ?: 1f) < 0.01) {
            explosionLights.onDispose()
            light?.isActive = false
            light = null
        }
    }

    private fun SpriteEntityWithLogic.updateExplosion() {
        val currentX = x
        val currentY = y
        explosion?.apply { this.effect.setPosition(currentX, currentY) }
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        body = gamePhysics.createDynamicBody()
        physicsShape = CircleShape().apply { radius = width / 2 }
        val fixture = body.createFixture(gamePhysics.getStandardFixtureDef(physicsShape))
        fixture?.userData = this
    }

}