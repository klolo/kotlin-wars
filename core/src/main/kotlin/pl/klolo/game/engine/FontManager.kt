package pl.klolo.game.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

enum class FontSize(val value: Int) {
    SMALL(18),
    MEDIUM(26),
    HUDE(36)
}

class FontManager {
    companion object {
        private val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("RuslanDisplay.ttf"))

        private val fontsBySize: Map<Int, BitmapFont> = mapOf(
                FontSize.SMALL.value to createBitmapFont(FontSize.SMALL.value),
                FontSize.MEDIUM.value to createBitmapFont(FontSize.MEDIUM.value),
                FontSize.HUDE.value to createBitmapFont(FontSize.HUDE.value)
        )

        fun getFontBySize(size: FontSize): BitmapFont {
            return fontsBySize[size.value] ?: throw IllegalArgumentException("Font value not found: $size")
        }

        private fun createBitmapFont(fontSize: Int) = fontGenerator
                .generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply { size = fontSize })
    }

}