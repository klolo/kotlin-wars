package pl.klolo.game.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor

open class TextEntity(entityConfiguration: EntityConfiguration, override var id: Int) : Entity, Actor() {
    private var font: BitmapFont
    var text: String = ""

    override val uniqueName = entityConfiguration.uniqueName
    override val layer: Int = entityConfiguration.layer
    override var useLighting: Boolean = false
    override var shouldBeRemove: Boolean = false

    init {
        x = entityConfiguration.x
        y = entityConfiguration.y
        width = entityConfiguration.width
        height = entityConfiguration.height

        font = BitmapFont()
        font.color = Color.WHITE
    }

    override fun dispose() {
        font.dispose()
    }

    override fun positionChanged() {
    }

    override fun draw(batch: Batch, camera: OrthographicCamera) {
        font.draw(batch, text, x, y);
    }

    override fun update(delta: Float) {
        super.act(delta)
    }

    fun getFontHeight(): Float {
        return font.lineHeight
    }
}