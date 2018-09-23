package pl.klolo.game.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Label
import pl.klolo.game.common.Colors.white
import pl.klolo.game.engine.GameEngine.Companion.applicationConfiguration

private val fontConfiguration = applicationConfiguration.getConfig("font")
enum class FontSize(val value: Int) {
    SMALL(fontConfiguration.getInt("small")),
    MEDIUM(fontConfiguration.getInt("medium")),
    BIG(fontConfiguration.getInt("big")),
    HUGE(fontConfiguration.getInt("huge")),
}

class FontManager {
    companion object {
        private val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("assets/ruslan-display.ttf"))

        private val fontsBySize: Map<Int, Label> = mapOf(
                FontSize.SMALL.value to createFont(FontSize.SMALL.value),
                FontSize.MEDIUM.value to createFont(FontSize.MEDIUM.value),
                FontSize.BIG.value to createFont(FontSize.BIG.value),
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