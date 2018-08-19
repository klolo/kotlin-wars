package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.engine.FontSize
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.entity.Entity
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.OnEnter
import pl.klolo.game.event.RegisterEntity
import pl.klolo.game.event.StartNewGame

class MainMenuLogic<T : Entity>(
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<T> {

    private val textConfiguration = entityRegistry.getConfigurationById("text")

    private val gameTitleLabel: TextEntity
            by lazy { initGameTitleLabel() }

    private val infoLabel: TextEntity
            by lazy { initInfoLabel() }

    override val initialize: T.() -> Unit = {
        eventProcessor
                .subscribe(id)
                .onEvent(OnEnter) {
                    eventProcessor.sendEvent(StartNewGame)
                }
    }

    override val onDispose: T.() -> Unit = {
        gameTitleLabel.dispose()
    }

    override val onUpdate: T.(Float) -> Unit = {
        gameTitleLabel.text = "Kotlin wars"
        gameTitleLabel.setPosition(
                Gdx.graphics.width.toFloat() / 2 - gameTitleLabel.getFontWidth() / 2,
                Gdx.graphics.height.toFloat() / 2
        )

        infoLabel.text = "(press enter for start, escape for exit)"
        infoLabel.setPosition(
                Gdx.graphics.width.toFloat() / 2 - infoLabel.getFontWidth() / 2,
                Gdx.graphics.height.toFloat() / 2 - gameTitleLabel.getFontHeight()
        )
    }

    private fun initGameTitleLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration, applicationContext)
                .apply {
                    fontSize = FontSize.HUDE
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
    }

    private fun initInfoLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration, applicationContext)
                .apply {
                    fontSize = FontSize.SMALL
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
    }

}