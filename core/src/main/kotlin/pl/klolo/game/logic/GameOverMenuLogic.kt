package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.engine.FontSize
import pl.klolo.game.engine.Highscore
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.entity.Entity
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.OnEnter
import pl.klolo.game.event.RegisterEntity
import pl.klolo.game.event.StartNewGame

class GameOverMenuLogic<T : Entity>(
        private val highscore: Highscore,
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<T> {

    val textConfiguration = entityRegistry.getConfigurationById("text")

    private val gameOverLabel: TextEntity
            by lazy { initGameOverLabel() }

    private val scoreLabels: List<TextEntity>
            by lazy { createScoreLabels() }

    override val initialize: T.() -> Unit = {
        eventProcessor
                .subscribe(id)
                .onEvent(OnEnter) {
                    eventProcessor.sendEvent(StartNewGame)
                }
    }

    override val onDispose: T.() -> Unit = {
    }

    override val onUpdate: T.(Float) -> Unit = {
        gameOverLabel.setPosition(
                Gdx.graphics.width.toFloat() / 2 - gameOverLabel.getFontWidth() / 2,
                Gdx.graphics.height.toFloat() / 2
        )

        for (i: Int in scoreLabels.indices) {
            val startYPosition = Gdx.graphics.height.toFloat() / 2 - gameOverLabel.getFontHeight()
            scoreLabels[i].setPosition(
                    Gdx.graphics.width.toFloat() / 2 - scoreLabels[i].getFontWidth() / 2,
                    startYPosition - (i * scoreLabels[i].getFontHeight())
            )
        }
    }

    private fun initGameOverLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration, applicationContext)
                .apply {
                    text = "Game over"
                    fontSize = FontSize.HUDE
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
    }

    private fun createScoreLabels(): List<TextEntity> {
        return highscore.getScore()
                .map {
                    createEntity<TextEntity>(textConfiguration, applicationContext)
                            .apply {
                                text = it.toString()
                                fontSize = FontSize.MEDIUM
                                eventProcessor.sendEvent(RegisterEntity(this))
                            }
                }
    }

}