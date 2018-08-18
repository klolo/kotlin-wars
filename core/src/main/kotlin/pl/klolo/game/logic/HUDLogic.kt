package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.GameLighting
import pl.klolo.game.applicationContext
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.EntityWithLogic
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.AddPoints
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity

class HUDLogic(
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<EntityWithLogic> {

    private val pointsLabel: TextEntity by lazy { initPointLabel() }
    private var points = 0

    private fun HUDLogic.initPointLabel(): TextEntity {
        val textEntityConfiguration = entityRegistry.getConfigurationById("text")

        if (textEntityConfiguration != null) {
            val result: TextEntity = createEntity(textEntityConfiguration, applicationContext)
            eventProcessor.sendEvent(RegisterEntity(result))
            return result
        }

        throw IllegalStateException("configuration for label not found")
    }

    override val onDispose: EntityWithLogic.() -> Unit = {

    }

    override val initialize: EntityWithLogic.() -> Unit = {
        eventProcessor
                .subscribe(id)
                .onEvent(AddPoints::class.java) {
                    pointsLabel.text = "${points++}"
                }
    }

    override val onUpdate: EntityWithLogic.(Float) -> Unit = {
        val leftMargin = 10f
        pointsLabel.setPosition(leftMargin, Gdx.graphics.height.toFloat() - pointsLabel.getFontHeight())
    }
}