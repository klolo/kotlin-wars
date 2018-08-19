package pl.klolo.game.entity

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import pl.klolo.game.engine.FontManager
import pl.klolo.game.engine.FontSize


open class TextEntity(entityConfiguration: EntityConfiguration, override var id: Int) : Entity, Actor() {
    override val uniqueName = entityConfiguration.uniqueName
    override val layer: Int = entityConfiguration.layer
    override var useLighting: Boolean = false
    override var shouldBeRemove: Boolean = false

    var text: String = ""
    var fontSize = FontSize.SMALL

    init {
        x = entityConfiguration.x
        y = entityConfiguration.y
        width = entityConfiguration.width
        height = entityConfiguration.height
    }

    override fun dispose() {
    }

    override fun positionChanged() {
    }

    override fun draw(batch: Batch, camera: OrthographicCamera) {
        FontManager.getFontBySize(fontSize).draw(batch, text, x, y)
    }

    override fun update(delta: Float) {
        super.act(delta)
    }

    fun getFontHeight(): Float {
        return FontManager.getFontBySize(fontSize).lineHeight
    }

    fun getFontWidth(): Float {
        return GlyphLayout(FontManager.getFontBySize(fontSize), text).width
    }
}