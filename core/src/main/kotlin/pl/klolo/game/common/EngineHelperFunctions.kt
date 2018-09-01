package pl.klolo.game.common

import com.badlogic.gdx.Gdx
import pl.klolo.game.configuration.Profile

fun getScreenWidth(profile: Profile): Float {
    return when (profile) {
        Profile.ANDROID -> Gdx.graphics.height.toFloat()
        else -> Gdx.graphics.width.toFloat()
    }
}

fun getScreenHeight(profile: Profile): Float {
    return when (profile) {
        Profile.ANDROID -> Gdx.graphics.width.toFloat()
        else -> Gdx.graphics.height.toFloat()
    }
}