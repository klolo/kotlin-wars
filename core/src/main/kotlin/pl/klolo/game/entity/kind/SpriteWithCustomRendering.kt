package pl.klolo.game.entity.kind

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import pl.klolo.game.entity.Entity
import pl.klolo.game.entity.EntityConfiguration
import pl.klolo.game.entity.EntityLogicWithRendering

open class SpriteWithCustomRendering(
        val entityConfiguration: EntityConfiguration,
        val logic: EntityLogicWithRendering<SpriteWithCustomRendering>,
        override var id: Int) : Entity, Actor() {

    override var useLighting: Boolean = true
    override val uniqueName = entityConfiguration.uniqueName
    override val layer: Int = entityConfiguration.layer

    override var shouldBeRemove: Boolean = false

    override fun dispose() {
        logic.onDispose.invoke(this)
    }

    override fun positionChanged() {
        super.positionChanged()
    }

    override fun draw(batch: Batch, camera: OrthographicCamera) {
        logic.draw.invoke(this, batch, camera)
    }

    override fun update(delta: Float) {
        logic.onUpdate.invoke(this, delta)
        super.act(delta)
    }
}