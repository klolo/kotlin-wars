package pl.klolo.game.logic

import box2dLight.Light
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import pl.klolo.game.configuration.Colors
import pl.klolo.game.engine.*
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.SpriteWithCustomRendering
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.OpenMainMenu
import pl.klolo.game.event.RegisterEntity
import pl.klolo.game.common.addSequence
import pl.klolo.game.common.execute
import pl.klolo.game.entity.EntityLogicWithRendering

class GameLoaderLogic(
        private val gameLighting: GameLighting,
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogicWithRendering<SpriteWithCustomRendering> {

    private val textConfiguration = entityRegistry.getConfigurationById("text")
    private lateinit var progressBar: Sprite
    private lateinit var progressBarFill: Sprite
    private lateinit var pulsingLightAnimation: PulsingLightAnimation
    private lateinit var infoLabel: TextEntity

    private var centerX = 0f
    private var centerY = 0f
    private var progress = 0f

    private val loaderDelay = .1f
    private lateinit var mainLight: Light

    override val initialize: SpriteWithCustomRendering.() -> Unit = {
        Gdx.app.debug(this.javaClass.name, "initialize")
        val loadingStart = System.currentTimeMillis()

        addSequence(
                delay(loaderDelay),
                execute {
                    progress += 0.2f
                    loadTextures()
                },
                delay(loaderDelay),
                execute {
                    progress += 0.2f
                    loadSounds()
                },
                delay(loaderDelay),
                execute {
                    progress += 0.2f
                    loadMusics()
                },
                execute {
                    progress += 0.2f
                    loadAtlases()
                },
                delay(loaderDelay),
                execute {
                    assetManager.finishLoading()
                    progress = 1f
                },
                delay(loaderDelay),
                execute {
                    Gdx.app.debug(this.javaClass.name, "Loading time: ${System.currentTimeMillis() - loadingStart} ms")
                    eventProcessor.sendEvent(OpenMainMenu)
                }
        )

        infoLabel = createInfoLabel()
        showProgressBar()
    }

    private fun loadAtlases() {
        atlas.forEach {
            println("load: $it")
            assetManager.load(it, TextureAtlas::class.java)
        }
    }

    private fun loadSounds() {
        sounds.forEach {
            println("load: $it")
            assetManager.load(it, Sound::class.java)
        }
    }

    private fun loadMusics() {
        musics.forEach {
            println("load: $it")
            assetManager.load(it, Music::class.java)
        }
    }

    private fun loadTextures() {
        textures.forEach {
            println("load: $it")
            assetManager.load(it, Texture::class.java)
        }
    }

    private fun showProgressBar() {
        centerX = Gdx.graphics.width.toFloat() / 2
        centerY = Gdx.graphics.height.toFloat() / 2

        val logoLight = gameLighting.createPointLight(200, Colors.blueLight, 300f, centerX, centerY)
        mainLight = gameLighting.createPointLight(200, Colors.ambient, 500f, centerX, centerY)

        pulsingLightAnimation = PulsingLightAnimation(logoLight)
                .apply {
                    delta = 1f
                    minDistance = 150
                    distanceGrow = 250
                }

        progressBar = Sprite(Texture(Gdx.files.internal("assets/lifebar-background.png")))
        progressBarFill = Sprite(Texture(Gdx.files.internal("assets/white.png")))
    }

    override val draw: SpriteWithCustomRendering.(batch: Batch, camera: OrthographicCamera) -> Unit = { batch, _ ->
        batch.draw(progressBarFill, centerX - infoLabel.getFontWidth() / 2, centerY, infoLabel.getFontWidth(), 10f)
        batch.draw(progressBar, centerX - infoLabel.getFontWidth() / 2, centerY, infoLabel.getFontWidth() * progress, 10f)
    }

    override val onDispose: SpriteWithCustomRendering.() -> Unit = {
        infoLabel.dispose()
    }

    override val onUpdate: SpriteWithCustomRendering.(Float) -> Unit = {
        pulsingLightAnimation.update()
    }

    private fun createInfoLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration)
                .apply {
                    text = "Kotlin wars"
                    fontSize = FontSize.BIG
                    useLighting = false
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
                .apply {
                    intializeFont()
                }
                .apply {
                    x = Gdx.graphics.width.toFloat() / 2 - getFontWidth() / 2
                    y = Gdx.graphics.height.toFloat() / 2 - (getFontHeight() * 2)
                }
    }
}