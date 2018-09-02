package pl.klolo.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import pl.klolo.game.engine.GameEngine
import pl.klolo.game.engine.assetManager

open class ParticleEntity(entityConfiguration: EntityConfiguration, override var id: Int) : Entity, Actor() {
    val effect = ParticleEffect()

    override var useLighting: Boolean = true
    override val uniqueName = entityConfiguration.uniqueName
    override val layer: Int = entityConfiguration.layer
    override var shouldBeRemove: Boolean = false

    private val entityScaleFactor = GameEngine.applicationConfiguration.getConfig("engine")
            .getDouble("entityScaleFactor")
            .toFloat()

    private val atlas = (assetManager.get("assets/damage.atlas", TextureAtlas::class.java))

    init {
        effect.load(Gdx.files.internal("assets/explosion.p"), atlas)
        effect.start()
        effect.scaleEffect(entityScaleFactor)
    }

    override fun dispose() {
    }

    override fun positionChanged() {
        super.positionChanged()
    }

    override fun draw(batch: Batch, camera: OrthographicCamera) {
        effect.draw(batch, Gdx.graphics.deltaTime)
    }

    override fun update(delta: Float) {
        effect.update(Gdx.graphics.deltaTime)
        effect.setPosition(x, y)
        super.act(delta)
    }
}