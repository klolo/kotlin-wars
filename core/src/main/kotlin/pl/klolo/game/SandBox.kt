package pl.klolo.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.GdxRuntimeException

class SandBox : ApplicationAdapter() {
    internal lateinit var batch: SpriteBatch
    internal lateinit var img: TextureRegion
    private var shaderOutline: ShaderProgram? = null

    internal var x = 0f
    internal var y = 0f
    internal var height = 256f
    internal var width = 256f
    internal var angle = 0f
    internal var outlineSize = 2f

    override fun create() {
        batch = SpriteBatch()
        img = TextureRegion(Texture("assets/shield1.png"))
        loadShader()
    }

    fun loadShader() {
        val vertexShader: String
        val fragmentShader: String

        vertexShader = Gdx.files.internal("shader/outline.glsl").readString()
        fragmentShader = Gdx.files.internal("shader/outline.frag").readString()

        shaderOutline = ShaderProgram(vertexShader, fragmentShader)
        if (!shaderOutline!!.isCompiled)
            throw GdxRuntimeException("Couldn't compile shader: " + shaderOutline!!.log)
    }

    override fun render() {
//        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0f)
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
//
//        shaderOutline!!.begin()
//        shaderOutline!!.setUniformf("u_viewportInverse", Vector2(1f / width, 1f / height))
//        shaderOutline!!.setUniformf("u_offset", outlineSize)
//        shaderOutline!!.setUniformf("u_step", Math.min(1f, width / 70f))
//        shaderOutline!!.setUniformf("u_color", Vector3(0f, 0f, 1f))
//        shaderOutline!!.end()
//        batch.shader = shaderOutline
//        batch.begin()
        batch.draw(img, x, y, width, height, width, height, 1f, 1f, angle)
//        batch.end()
//        batch.shader = null
    }
}