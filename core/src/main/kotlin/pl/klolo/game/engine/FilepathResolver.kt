package pl.klolo.game.engine

import pl.klolo.game.configuration.Profile

class FilepathResolver(private val profile: Profile) {
    fun resolve(path: String): String {
        return when (profile) {
            Profile.DESKTOP, Profile.WEB -> path
            Profile.ANDROID -> "assets/$path"
        }
    }
}