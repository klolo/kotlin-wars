package pl.klolo.game.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import pl.klolo.game.configuration.Colors
import pl.klolo.game.engine.FontManager
import pl.klolo.game.engine.FontSize

open class TextEntity(entityConfiguration: EntityConfiguration, override var id: Int) : Entity, Actor() {
    override val uniqueName = entityConfiguration.uniqueName
    override val layer: Int = entityConfiguration.layer
    override var useLighting: Boolean = false
    override var shouldBeRemove: Boolean = false

    var label: Label? = null
    var text: String = ""
    var fontSize = FontSize.SMALL
    var labelColor = Colors.white

    init {
        x = entityConfiguration.x
        y = entityConfiguration.y
        width = entityConfiguration.width
        height = entityConfiguration.height
    }

    fun intializeFont() {
        label = FontManager.getFontBySize(fontSize)
        label?.setSize(fontSize.value.toFloat(), fontSize.value.toFloat())
        label?.setText(text)
        color.a = 0.9f
    }

    override fun dispose() {
    }

    override fun positionChanged() {
    }

    override fun draw(batch: Batch, camera: OrthographicCamera) {
        label?.setPosition(x, y)
        label?.scaleBy(scaleX, scaleY)
        label?.setText(text)
        label?.color = labelColor;
        label?.draw(batch, color.a)
    }

    override fun update(delta: Float) {
        super.act(delta)
    }

    fun getFontHeight(): Float {
        return FontManager.getFontBySize(fontSize).height
    }

    fun getFontWidth(): Float {
        return GlyphLayout(label?.style?.font, text).width
    }
}