package pl.klolo.game.entity

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import pl.klolo.game.logic.EntityLogic

open class SpriteEntityWithLogic(
        override val uniqueId: String,
        private val logic: EntityLogic<SpriteEntityWithLogic>,
        private var sprite: Sprite,
        override var layer: Int) : Entity, Actor() {
    override var shouldBeRemove: Boolean = false

    override fun dispose() {
        sprite.texture.dispose()
    }

    override fun positionChanged() {
        sprite.setPosition(x, y)
        super.positionChanged()
    }

    override fun draw(batch: Batch, camera: OrthographicCamera) {
        batch.draw(sprite, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
    }

    override fun update(delta: Float) {
        logic.onUpdate.invoke(this, delta)
        super.act(delta)
    }
}