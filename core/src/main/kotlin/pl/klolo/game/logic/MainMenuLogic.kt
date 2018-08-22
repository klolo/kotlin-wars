package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
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
    private lateinit  var rocket: Sprite
    private val textConfiguration = entityRegistry.getConfigurationById("text")

    private val gameTitleLabel: TextEntity
            by lazy { initGameTitleLabel() }

    private val infoLabel: TextEntity
            by lazy { initInfoLabel() }

    override val initialize: T.() -> Unit = {
        println("MainMenuLogic creating...")

        rocket = Sprite(Texture(Gdx.files.internal("rocket.png")))
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
        gameTitleLabel.setPosition(
                Gdx.graphics.width.toFloat() / 2 - gameTitleLabel.getFontWidth() / 2,
                Gdx.graphics.height.toFloat() / 2
        )

        infoLabel.setPosition(
                Gdx.graphics.width.toFloat() / 2 - infoLabel.getFontWidth() / 2,
                Gdx.graphics.height.toFloat() / 2 - gameTitleLabel.getFontHeight()
        )
    }

    private fun initGameTitleLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration, applicationContext)
                .apply {
                    fontSize = FontSize.HUDE
                    text = "kotlin wars"
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
    }

    private fun initInfoLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration, applicationContext)
                .apply {
                    text = "press enter for start, escape for exit"
                    fontSize = FontSize.SMALL
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
    }

}