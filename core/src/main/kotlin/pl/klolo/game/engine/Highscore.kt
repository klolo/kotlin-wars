package pl.klolo.game.engine

import com.badlogic.gdx.Gdx

class Highscore {
    private var lastScore = 0
    private val prefs by lazy { Gdx.app.getPreferences("kotlin-wars-highscore") }

    fun getRecord(): Int = prefs.getInteger("record")

    fun getLastScore(): Int = lastScore

    fun setLastScore(score: Int) {
        lastScore = score
        if (prefs.getInteger("record") < score) {
            prefs.putInteger("record", score)
            prefs.flush()
        }
    }
}