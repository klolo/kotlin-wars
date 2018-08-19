package pl.klolo.game.engine

class Highscore {
    private var result = emptyList<Int>()

    fun addScore(score: Int) {
        result += score
    }

    fun getScore(): List<Int> {
        return result.sortedDescending()
    }
}