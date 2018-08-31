package pl.klolo.game.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Label
import pl.klolo.game.configuration.Colors.white

enum class FontSize(val value: Int) {
    SMALL(20),
    MEDIUM(26),
    BIG(45),
    HUGE(80),
}

class FontManager {
    companion object {
        private val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("RuslanDisplay.ttf"))

        private val fontsBySize: Map<Int, Label> = mapOf(
                FontSize.SMALL.value to createFont(FontSize.SMALL.value),
                FontSize.MEDIUM.value to createFont(FontSize.MEDIUM.value),
                FontSize.HUGE.value to createFont(FontSize.HUGE.value)
        )

        fun getFontBySize(size: FontSize): Label {
            return fontsBySize[size.value] ?: throw IllegalArgumentException("Font value not found: $size")
        }

        private fun createFont(fontSize: Int): Label {
            val bitmapFont = fontGenerator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply { size = fontSize })
            return Label("", Label.LabelStyle(bitmapFont, white))
        }
    }
}