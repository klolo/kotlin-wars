package pl.klolo.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import pl.klolo.game.event.EventProcessor

class GameEngine internal constructor(val eventProcessor: EventProcessor) : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture
    private val applicationConfiguration = ConfigFactory.load()

    fun getConfig(name: String): Config = applicationConfiguration.getConfig(name)

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")

        Gdx.input.inputProcessor = KeyboardProcessor()
    }

    override fun dispose() {
        img.dispose()
        batch.dispose()
    }

    override fun render() {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(img, 10f, 10f)
        batch.end()
    }
}