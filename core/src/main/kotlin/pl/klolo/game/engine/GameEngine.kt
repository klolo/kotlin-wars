package pl.klolo.game.engine

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import pl.klolo.game.physics.GamePhysics

class GameEngine internal constructor(
        private val gamePhysics: GamePhysics,
        private val gameLighting: GameLighting,
        private val inputProcessor: InputProcessor,
        private val stage: Stage) : ApplicationAdapter() {

    private lateinit var batch: SpriteBatch
    private lateinit var camera: OrthographicCamera

    companion object {
        val applicationConfiguration: Config = ConfigFactory.load()
    }

    fun getConfig(name: String): Config = applicationConfiguration.getConfig(name)

    override fun create() {
        Gdx.input.inputProcessor = inputProcessor
        Gdx.app.logLevel = Application.LOG_DEBUG;

        gamePhysics.initPhysics()
        gameLighting.initLights()
        batch = SpriteBatch()

        stage.initEntities()

        initializeCamera()
    }

    override fun resize(width: Int, height: Int) {
        batch.projectionMatrix = camera.combined
    }

    override fun dispose() {
        batch.dispose()
        gameLighting.dispose()
        gamePhysics.dispose()
    }

    override fun render() {
        stage.update(Gdx.graphics.deltaTime)

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        stage.drawWithLight(batch, camera)
        batch.end()

        gameLighting.render(camera)

        batch.begin()
        stage.drawWithoutLight(batch, camera)
        batch.end()

        gamePhysics.update()
        gamePhysics.debugRender(camera.combined)
    }

    private fun initializeCamera() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()) // TODO: ustawianie rozdzielczosci gry
        batch.projectionMatrix = camera.combined
    }
}