package pl.klolo.game.entity

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import pl.klolo.game.logic.EmptyLogic
import pl.klolo.game.logic.EntityLogic
import kotlin.properties.Delegates

open class TextureEntity() : Entity {
    override var uniqueId: String by Delegates.notNull()
    var logic: EntityLogic<TextureEntity> = EmptyLogic()

    var texture: Texture by Delegates.notNull()

    override var bounds: Bounds by Delegates.notNull()

    constructor(init: TextureEntity.() -> Unit) : this() {
        this.init()
    }

    override fun dispose() {
        texture.dispose()
    }

    override fun draw(batch: Batch, camera: OrthographicCamera) {
        batch.draw(texture, bounds.x, bounds.y, 250f, 250f)
    }

    override fun update(delta: Float) {
        logic.onUpdate.invoke(this, delta)
    }
}