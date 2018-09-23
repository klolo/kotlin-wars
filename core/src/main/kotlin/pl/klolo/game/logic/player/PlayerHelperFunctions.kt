package pl.klolo.game.logic.player

import pl.klolo.game.engine.Profile

fun getPlayerBottomMargin(profile: Profile, playerHeight: Float): Float {
    return when (profile) {
        Profile.ANDROID -> playerHeight * 1.5f
        else -> playerHeight
    }
}