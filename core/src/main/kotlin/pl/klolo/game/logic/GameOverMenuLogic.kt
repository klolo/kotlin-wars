package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.engine.FontSize
import pl.klolo.game.engine.Highscore
import pl.klolo.game.entity.Entity
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.*

class GameOverMenuLogic<T : Entity>(
        private val highscore: Highscore,
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<T> {

    private val textConfiguration = entityRegistry.getConfigurationById("text")
    private lateinit var infoLabel: TextEntity
    private lateinit var gameOverLabel: TextEntity
    private lateinit var scoreLabels: TextEntity

    override val initialize: T.() -> Unit = {
        Gdx.app.debug(this.javaClass.name, "createSubscription")
        eventProcessor
                .subscribe(id)
                .onEvent(OnEnter) { eventProcessor.sendEvent(StartNewGame) }
                .onEvent(OnEscape) { Gdx.app.exit() }

        infoLabel = createInfoLabel()
        gameOverLabel = createGameOverLabel()
        scoreLabels = createScoreLabels()
    }

    override val onDispose: T.() -> Unit = {
    }

    override val onUpdate: T.(Float) -> Unit = {
    }

    private fun createGameOverLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration)
                .apply {
                    text = "Game over"
                    fontSize = FontSize.HUGE
                    eventProcessor.sendEvent(RegisterEntity(this))
                    intializeFont()
                }
                .apply {
                    x = Gdx.graphics.width.toFloat() / 2 - getFontWidth() / 2
                    y = Gdx.graphics.height.toFloat() / 2
                }
    }

    private fun createScoreLabels(): TextEntity {
        val startYPosition = Gdx.graphics.height.toFloat() / 2 - gameOverLabel.getFontHeight()

        return createEntity<TextEntity>(textConfiguration)
                .apply {
                    text = "Your score: ${highscore.getLastScore()}\n\nBest score: ${highscore.getRecord()}"
                    fontSize = FontSize.SMALL
                    eventProcessor.sendEvent(RegisterEntity(this))
                    intializeFont()
                }
                .apply {
                    x = Gdx.graphics.width.toFloat() / 2 - getFontWidth() / 2
                    y = startYPosition - getFontHeight() * 2
                }
    }

    private fun createInfoLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration)
                .apply {
                    text = "press enter for start, escape for exit"
                    fontSize = FontSize.SMALL
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
                .apply {
                    intializeFont()
                }
                .apply {
                    x = Gdx.graphics.width.toFloat() / 2 - getFontWidth() / 2
                    y = getFontHeight()
                }
    }

}