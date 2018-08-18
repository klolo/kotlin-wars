package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import pl.klolo.game.GameLighting
import pl.klolo.game.applicationContext
import pl.klolo.game.entity.EntityConfiguration
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.*

class PlayerLogic(
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {

    private lateinit var playerLight: PointLight

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {

    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        x = Gdx.graphics.width.toFloat() / 2 - width / 2
        playerLight = gameLighting.createPointLight(100, "#9adde3ff", 80f, x, y)
        eventProcessor
                .subscribe(id)
                .onEvent(OnLeft) {
                    if (x - 100 > 0) {
                        addAction(moveTo(x - 100, y, .25f))
                    }
                }
                .onEvent(OnRight) {
                    if (x + 100 < Gdx.graphics.width.toFloat()) {
                        addAction(moveTo(x + 100, y, .25f))
                    }
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

        val eventToSend = RegisterEntity(bulletEntity)
        eventProcessor.sendEvent(eventToSend)
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        playerLight.setPosition(x + width / 2, y + height / 2)
    }
}