package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.engine.FontSize
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.entity.Entity
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.*

class MainMenuLogic<T : Entity>(
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<T> {
    private val textConfiguration = entityRegistry.getConfigurationById("text")

    private val gameTitleLabel: TextEntity
            by lazy { initGameTitleLabel() }

    private val infoLabel: TextEntity
            by lazy { initInfoLabel() }

    override val initialize: T.() -> Unit = {
        println("MainMenuLogic creating...")

        eventProcessor
                .subscribe(id)
                .onEvent(OnEnter) { eventProcessor.sendEvent(StartNewGame) }
                .onEvent(OnEscape) { Gdx.app.exit() }


    }

    override val onDispose: T.() -> Unit = {
        gameTitleLabel.dispose()
    }

    override val onUpdate: T.(Float) -> Unit = {
        val width = Gdx.graphics.width.toFloat()
        val height = Gdx.graphics.height.toFloat()
        gameTitleLabel.setPosition(
                width / 2 - gameTitleLabel.getFontWidth() / 2,
                height / 2
        )

        infoLabel.setPosition(
                width / 2 - infoLabel.getFontWidth() / 2,
                gameTitleLabel.getFontHeight()
        )

    }

    private fun initGameTitleLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration, applicationContext)
                .apply {
                    fontSize = FontSize.HUGE
                    text = "kotlin wars"
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
                .apply {
                    intializeFont()
                }
    }

    private fun initInfoLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration, applicationContext)
                .apply {
                    text = "press enter for start, escape for exit"
                    fontSize = FontSize.SMALL
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
                .apply {
                    intializeFont()
                }
    }

}