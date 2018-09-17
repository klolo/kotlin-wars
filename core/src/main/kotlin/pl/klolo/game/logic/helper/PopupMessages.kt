package pl.klolo.game.logic.helper

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity
import pl.klolo.game.common.addSequence
import pl.klolo.game.common.execute
import pl.klolo.game.configuration.Colors

data class PopupMessageConfiguration(
        val message: String,
        val color: Color = Colors.white,
        val callback: () -> Unit = {}
)

class PopupMessages(
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor) {

    private var messageLabel: TextEntity? = null
    private val textEntityConfiguration = entityRegistry.getConfigurationById("text")

    var show: SpriteEntityWithLogic.(popupMessageConfiguration: PopupMessageConfiguration) -> Unit = { popupMessageConfiguration ->
        if (messageLabel != null) {
            (messageLabel as TextEntity).shouldBeRemove = true
        }

        messageLabel = createEntity<TextEntity>(textEntityConfiguration)
                .apply {
                    text = popupMessageConfiguration.message
                    eventProcessor.sendEvent(RegisterEntity(this))
                    intializeFont()
                    labelColor = popupMessageConfiguration.color
                }
                .apply {
                    addSequence(
                            alpha(0f, 0.01f),
                            alpha(1f, 0.1f),
                            alpha(0f, 1f),
                            execute {
                                messageLabel?.shouldBeRemove = true
                                messageLabel = null
                                popupMessageConfiguration.callback()
                            })
                }

        messageLabel!!.setPosition(x + messageLabel!!.width / 2, y + height)
    }

    fun updatePosition(posX: Float, posY: Float) {
        messageLabel?.setPosition(posX - (messageLabel!!.getFontWidth() / 2), posY)
    }
}