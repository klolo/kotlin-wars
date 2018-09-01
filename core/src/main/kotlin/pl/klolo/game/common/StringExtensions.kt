package pl.klolo.game.common

import com.badlogic.gdx.graphics.Color
import java.lang.Long.parseLong

fun String.toColor(): Color {
    val r = parseLong(this.subSequence(1, 3).toString(), 16).toFloat() / 255
    val g = parseLong(this.subSequence(3, 5).toString(), 16).toFloat() / 255
    val b = parseLong(this.subSequence(5, 7).toString(), 16).toFloat() / 255
    return Color(r, g, b, 1f)
}