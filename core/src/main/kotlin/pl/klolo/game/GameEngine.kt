package pl.klolo.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import pl.klolo.game.event.EventProcessor

class GameEngine internal constructor(
        private val eventProcessor: EventProcessor,
        private val inputProcessor: InputProcessor,
        private val stage: Stage,
        private val applicationConfiguration: Config = ConfigFactory.load()) : ApplicationAdapter() {

    private lateinit var batch: SpriteBatch
    private lateinit var camera: OrthographicCamera

    fun getConfig(name: String): Config = applicationConfiguration.getConfig(name)

    override fun create() {
        Gdx.input.inputProcessor = inputProcessor

        batch = SpriteBatch()

        initializeCamera()

        stage.loadStage()
    }

    override fun resize(width: Int, height: Int) {
        batch.projectionMatrix = camera.combined
    }

    override fun dispose() {
        batch.dispose()
    }

    override fun render() {
        stage.update(Gdx.graphics.deltaTime)

        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        stage.draw(batch, camera)
        batch.end()
    }

    private fun initializeCamera() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, 1920f, 1080f); // TODO: ustawianie rozdzielczosci gry
        batch.projectionMatrix = camera.combined
    }
}