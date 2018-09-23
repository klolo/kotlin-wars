package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import pl.klolo.game.common.executeAfterDelay
import pl.klolo.game.engine.Profile
import pl.klolo.game.engine.FontSize
import pl.klolo.game.engine.Highscore
import pl.klolo.game.engine.ProfileHolder
import pl.klolo.game.entity.*
import pl.klolo.game.entity.kind.TextEntity
import pl.klolo.game.event.*

class GameOverMenuLogic<T : Entity>(
        private val profileHolder: ProfileHolder,
        private val highscore: Highscore,
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<T>, Actor() {

    private val textConfiguration = entityRegistry.getConfigurationById("text")
    private lateinit var infoLabel: TextEntity
    private lateinit var gameOverLabel: TextEntity
    private lateinit var scoreLabels: TextEntity
    private val activateNavigationTime = 1f

    override val initialize: T.() -> Unit = {
        Gdx.app.debug(this.javaClass.name, "createSubscription")

        executeAfterDelay(activateNavigationTime) {
            eventProcessor
                    .subscribe(id)
                    .onEvent<PressedEnter> { eventProcessor.sendEvent(StartNewGame) }
                    .onEvent<PressedEscape> { Gdx.app.exit() }
        }

        infoLabel = createInfoLabel()
        gameOverLabel = createGameOverLabel()
        scoreLabels = createScoreLabels()
    }

    override val onDispose: T.() -> Unit = {
    }

    override val onUpdate: T.(Float) -> Unit = {
        super.act(it)
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
                    text = if (profileHolder.activeProfile == Profile.ANDROID) "touch to start" else "press enter for start, escape for exit"
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