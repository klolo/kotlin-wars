package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import pl.klolo.game.GameLighting
import pl.klolo.game.applicationContext
import pl.klolo.game.entity.EntityConfiguration
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.*
import pl.klolo.game.extensions.execute

class PlayerLogic(
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {
    private var currentMove: Action = execute(Runnable {})
    private lateinit var playerLight: PointLight

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {

    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        x = Gdx.graphics.width.toFloat() / 2 - width / 2
        playerLight = gameLighting.createPointLight(100, "#9adde3ff", 80f, x, y)
        eventProcessor
                .subscribe(id)
                .onEvent(OnLeftDown) {
                    if (x - 100 > 0) {
                        removeAction(currentMove)
                        currentMove = moveTo(x - Gdx.graphics.width.toFloat(), y, 5f)
                        addAction(currentMove)
                    }
                }
                .onEvent(OnRightDown) {
                    if (x + 100 < Gdx.graphics.width.toFloat()) {
                        removeAction(currentMove)
                        currentMove = moveTo(x + Gdx.graphics.width.toFloat(), y, 5f)
                        addAction(currentMove)
                    }
                }
                .onEvent(OnRightUp) {
                    removeAction(currentMove)
                }
                .onEvent(OnLeftUp) {
                    removeAction(currentMove)
                }
                .onEvent(OnSpace) {
                    val laserConfiguration = entityRegistry.getConfigurationById("laserBlue01")
                    if (laserConfiguration != null) {
                        shootOnPosition(laserConfiguration)
                    }
                }
                .onEvent(OnCollision::class.java) {

                }
    }

    private fun SpriteEntityWithLogic.shootOnPosition(laserConfiguration: EntityConfiguration) {
        val bulletXPosition = x + width / 2
        val bulletYPosition = y + height / 2

        val bulletEntity: SpriteEntityWithLogic = createEntity(laserConfiguration, applicationContext) {
            x = bulletXPosition
            y = bulletYPosition
        } as SpriteEntityWithLogic

        eventProcessor.sendEvent(RegisterEntity(bulletEntity))
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        playerLight.setPosition(x + width / 2, y + height / 2)
    }
}