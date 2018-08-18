package pl.klolo.game.entity

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import pl.klolo.game.logic.EntityLogic

open class EntityWithLogic(entityConfiguration: EntityConfiguration,
                           val logic: EntityLogic<EntityWithLogic>,
                           override var id: Int) : Entity, Actor() {

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
    }

    override fun update(delta: Float) {
        logic.onUpdate.invoke(this, delta)
        super.act(delta)
    }
}