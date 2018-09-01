package pl.klolo.game.entity

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor

open class SpriteEntityWithLogic(
        entityConfiguration: EntityConfiguration,
        val logic: EntityLogic<SpriteEntityWithLogic>,
        val sprite: Sprite,
        override var id: Int) : Entity, Actor() {
    override var useLighting: Boolean = true
    override val uniqueName = entityConfiguration.uniqueName
    override val layer: Int = entityConfiguration.layer
    override var shouldBeRemove: Boolean = false
    var display = true

    init {
        x = entityConfiguration.x
        y = entityConfiguration.y
        width = entityConfiguration.width
        height = entityConfiguration.height
        sprite.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    override fun dispose() {
        logic.onDispose.invoke(this)
    }

    override fun positionChanged() {
        sprite.setPosition(x, y)
        super.positionChanged()
    }

    override fun draw(batch: Batch, camera: OrthographicCamera) {
        if (display) {
            batch.draw(sprite, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
        }
    }

    override fun update(delta: Float) {
        logic.onUpdate.invoke(this, delta)
        super.act(delta)
    }
}