package pl.klolo.game.logic.player

import pl.klolo.game.configuration.Profile

fun getPlayerBottomMargin(profile: Profile, playerHeight: Float): Float {
    return when (profile) {
        Profile.ANDROID -> playerHeight * 3
        else -> playerHeight
    }
}