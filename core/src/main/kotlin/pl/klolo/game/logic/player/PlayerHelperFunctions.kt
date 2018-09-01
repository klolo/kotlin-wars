package pl.klolo.game.logic.player

import com.badlogic.gdx.Gdx
import pl.klolo.game.configuration.Profile

fun getPlayerBottomMargin(profile: Profile, playerHeight: Float): Float {
    return when (profile) {
        Profile.ANDROID -> playerHeight * 2
        else -> playerHeight
    }
}