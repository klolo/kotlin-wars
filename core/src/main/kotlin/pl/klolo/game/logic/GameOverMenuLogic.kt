package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.engine.FontSize
import pl.klolo.game.engine.Highscore
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.entity.Entity
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.*

class GameOverMenuLogic<T : Entity>(
        private val highscore: Highscore,
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<T> {

    val textConfiguration = entityRegistry.getConfigurationById("text")

    private val gameOverLabel: TextEntity
            by lazy { initGameOverLabel() }

    private val scoreLabels: TextEntity
            by lazy { createScoreLabels() }

    override val initialize: T.() -> Unit = {
        Gdx.app.debug(this.javaClass.name,"initialize")
        eventProcessor
                .subscribe(id)
                .onEvent(OnEnter) { eventProcessor.sendEvent(StartNewGame) }
                .onEvent(OnEscape) { Gdx.app.exit() }
    }

    override val onDispose: T.() -> Unit = {
    }

    override val onUpdate: T.(Float) -> Unit = {
        gameOverLabel.setPosition(
                Gdx.graphics.width.toFloat() / 2 - gameOverLabel.getFontWidth() / 2,
                Gdx.graphics.height.toFloat() / 2
        )

        val startYPosition = Gdx.graphics.height.toFloat() / 2 - gameOverLabel.getFontHeight()
        scoreLabels.setPosition(
                Gdx.graphics.width.toFloat() / 2 - scoreLabels.getFontWidth() / 2,
                startYPosition - scoreLabels.getFontHeight() * 2)

    }

    private fun initGameOverLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration)
                .apply {
                    text = "Game over"
                    fontSize = FontSize.HUGE
                    eventProcessor.sendEvent(RegisterEntity(this))
                    intializeFont()
                }
    }

    private fun createScoreLabels(): TextEntity {
        return createEntity<TextEntity>(textConfiguration)
                .apply {
                    text = "Your score: ${highscore.getLastScore()}\n\nBest score: ${highscore.getRecord()}"
                    fontSize = FontSize.SMALL
                    eventProcessor.sendEvent(RegisterEntity(this))
                    intializeFont()
                }
    }

}