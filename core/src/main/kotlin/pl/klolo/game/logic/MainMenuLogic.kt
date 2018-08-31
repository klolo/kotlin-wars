package pl.klolo.game.logic

import box2dLight.Light
import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import pl.klolo.game.configuration.Colors
import pl.klolo.game.engine.FontSize
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.engine.Song
import pl.klolo.game.engine.SoundManager
import pl.klolo.game.entity.*
import pl.klolo.game.event.*


class PulsingLightAnimation(private val light: Light) {
    private var delta = 0f
    private val deltaStep = 0.02f
    private val minDistance = 150
    private val distanceGrow = 250

    fun update() {
        delta += deltaStep
        light.distance = minDistance + (distanceGrow * Math.abs(Math.sin(delta.toDouble())).toFloat())
    }

    fun dispose() {
        light.remove()
    }
}

class MainMenuLogic<T : Entity>(
        private val gameLighting: GameLighting,
        private val soundManager: SoundManager,
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<T> {

    private val textConfiguration = entityRegistry.getConfigurationById("text")

    private lateinit var pulsingLightAnimation: PulsingLightAnimation
    private lateinit var gameTitleLabel: TextEntity
    private lateinit var infoLabel: TextEntity

    override val initialize: T.() -> Unit = {
        Gdx.app.debug(this.javaClass.name, "initialize")

        eventProcessor
                .subscribe(id)
                .onEvent(OnEnter) { eventProcessor.sendEvent(StartNewGame) }
                .onEvent(OnEscape) { Gdx.app.exit() }

        soundManager.playSong(Song.MENU)
        infoLabel = createInfoLabel()
        gameTitleLabel = createGameTitleLabel()
        showGameLogo()
    }

    private fun showGameLogo() {
        val logoConfiguration = entityRegistry.getConfigurationById("gameLogo")
        val gameLogo: SpriteEntityWithLogic = createEntity(logoConfiguration) {
            width = logoConfiguration.width
            height = logoConfiguration.height
            x = Gdx.graphics.width.toFloat() / 2 - width / 2
            y = Gdx.graphics.height.toFloat() / 2 - height * 2
        }

        val logoLight = gameLighting.createPointLight(100, Colors.white, 300f,
                Gdx.graphics.width.toFloat() / 2, gameLogo.y + gameLogo.height / 2)
        pulsingLightAnimation = PulsingLightAnimation(logoLight)

        eventProcessor.sendEvent(RegisterEntity(gameLogo))
    }

    override val onDispose: T.() -> Unit = {
        gameTitleLabel.dispose()
        pulsingLightAnimation.dispose()
    }

    override val onUpdate: T.(Float) -> Unit = {
        pulsingLightAnimation.update()
    }

    private fun createGameTitleLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration)
                .apply {
                    fontSize = FontSize.HUGE
                    text = "kotlin wars"
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
                .apply {
                    intializeFont()
                }
                .apply {
                    x = Gdx.graphics.width.toFloat() / 2 - getFontWidth() / 2
                    y = Gdx.graphics.height.toFloat() / 2 + getFontHeight()
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