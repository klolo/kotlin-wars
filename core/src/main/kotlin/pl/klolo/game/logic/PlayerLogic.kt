package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.engine.Highscore
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.engine.isEnemyLaser
import pl.klolo.game.entity.EntityConfiguration
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.*
import pl.klolo.game.extensions.execute
import pl.klolo.game.physics.GamePhysics


class PlayerLogic(
        private val highscore: Highscore,
        private val gamePhysics: GamePhysics,
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {

    private val state = PlayerState()

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        state.physicsShape.dispose()
        state.playerLight.remove()
        gamePhysics.destroy(state.body)
    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        println("PlayerLogic creating...")

        x = Gdx.graphics.width.toFloat() / 2 - width / 2
        state.playerLight = gameLighting.createPointLight(100, "#9adde3ff", 70f, x, y)
        state.laserConfiguration = entityRegistry.getConfigurationById("laserBlue01")

        val playerSpeed = 3f // seconds per screen width
        createPhysics()

        eventProcessor
                .subscribe(id)
                .onEvent(OnLeftDown) {
                    if (x - 100 > 0) {
                        onMove(x - Gdx.graphics.width.toFloat(), playerSpeed)
                    }
                }
                .onEvent(OnRightDown) {
                    if (x + 100 < Gdx.graphics.width.toFloat()) {
                        onMove(x + Gdx.graphics.width.toFloat(), playerSpeed)
                    }
                }
                .onEvent(OnRightUp) {
                    removeAction(state.currentMove)
                }
                .onEvent(OnLeftUp) {
                    removeAction(state.currentMove)
                }
                .onEvent(OnSpace) {
                    shootOnPosition(state.laserConfiguration)
                }
                .onEvent(AddPoints::class.java) {
                    state.points += it.points
                }
                .onEvent(OnCollision::class.java) {
                    onCollision(it)
                }
                .onEvent(AddPlayerLife::class.java) {
                    println("increase life level ${it}")
                    state.lifeLevel += it.lifeAmount
                    if (state.lifeLevel > 100) {
                        state.lifeLevel = 100
                    }
                    eventProcessor.sendEvent(ChangePlayerLfeLevel(state.lifeLevel))
                }
                .onEvent(EnableSuperBullet) {
                    enableSuperBullet()
                    addAction(sequence(
                            delay(15f),
                            execute(Runnable { disableSuperBullet() }
                            )))
                }
                .onEvent(EnableShield) {

                }
                .onEvent(EnableDoublePoints) {

                }
    }

    private fun enableSuperBullet() {
        println("Enable super bullet. enabled: ${state.enabledSuperBulletCounter}, current power: ${state.bulletPower}")
        state.laserConfiguration = entityRegistry.getConfigurationById("laserBlue02")
        state.bulletPower *= 2
        state.enabledSuperBulletCounter++
        state.playerLight.distance = 100f
    }

    private fun disableSuperBullet() {
        state.enabledSuperBulletCounter--
        if (state.enabledSuperBulletCounter == 0) {
            println("Disable super bullet.")
            state.laserConfiguration = entityRegistry.getConfigurationById("laserBlue01")
            state.bulletPower = state.defaulBulletPower
            state.playerLight.distance = 70f
        }
    }

    private fun SpriteEntityWithLogic.onMove(x: Float, playerSpeed: Float) {
        removeAction(state.currentMove)
        state.currentMove = moveTo(x, y, playerSpeed)
        addAction(state.currentMove)
    }

    private fun onCollision(it: OnCollision) {
        val collidedEntity = it.entity!!
        if (isEnemyLaser(collidedEntity)) {
            state.lifeLevel -= 10

            // TODO: animation
            //                        addAction(
            //                                sequence(
            //                                        scaleTo(0.9f, 0.9f, 0.1f, Interpolation.bounce),
            //                                        scaleTo(1f, 1f, 0.1f, Interpolation.bounce)
            //                                )
            //                        )

            eventProcessor.sendEvent(ChangePlayerLfeLevel(state.lifeLevel))

            if (state.lifeLevel <= 0) {
                highscore.setLastScore(state.points)
                eventProcessor.sendEvent(GameOver(state.lifeLevel))
            }
        }
    }

    private fun SpriteEntityWithLogic.shootOnPosition(laserConfiguration: EntityConfiguration) {
        val bulletXPosition = x + width / 2
        val bulletYPosition = y + height / 2

        val bulletEntity: SpriteEntityWithLogic = createEntity(laserConfiguration, applicationContext) {
            x = bulletXPosition
            y = bulletYPosition
        }

        (bulletEntity.logic as BulletLogic).isEnemyBullet = false
        bulletEntity.logic.bulletPower = state.bulletPower

        eventProcessor.sendEvent(RegisterEntity(bulletEntity))
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        state.playerLight.setPosition(x + width / 2, y + height / 2)
        state.body.setTransform(x + width / 2, y + height / 2, 0.0f)
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        state.physicsShape = PolygonShape()
        state.physicsShape.setAsBox(width / 2, height / 2);
        state.body = gamePhysics.createDynamicBody()

        state.body
                .createFixture(gamePhysics.getStandardFixtureDef(state.physicsShape))
                .userData = this
    }
}