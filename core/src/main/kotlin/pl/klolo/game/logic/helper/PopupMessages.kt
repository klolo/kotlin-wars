package pl.klolo.game.logic.helper

import com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity
import pl.klolo.game.extensions.addSequence
import pl.klolo.game.extensions.execute

class PopupMessages(
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor) {

    private var messageLabel: TextEntity? = null

    var show: SpriteEntityWithLogic.(message: String) -> Unit = { message: String ->
        showAndRun(message, {})
    }

    var showAndRun: SpriteEntityWithLogic.(message: String, () -> Unit) -> Unit = { message: String, callback: () -> Unit ->
        if (messageLabel != null) {
            (messageLabel as TextEntity).shouldBeRemove = true
        }

        messageLabel = createResultLabel(message)
        messageLabel?.setPosition(x, y + height)
        messageLabel?.apply {
            addSequence(
                    alpha(0f, 0.01f),
                    alpha(1f, 0.1f),
                    alpha(0f, 1f),
                    execute {
                        (messageLabel as TextEntity).shouldBeRemove = true
                        messageLabel = null
                        callback()
                    })
        }
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