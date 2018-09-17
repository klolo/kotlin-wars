package pl.klolo.game.logic

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import pl.klolo.game.logic.player.move.AndroidPlayerMoveLogic

internal class AndroidPlayerMoveLogicTest : ShouldSpec({

    "stand in place when acceleroment is smaller than 1" {
        val speed = AndroidPlayerMoveLogic.getPlayerSpeed(0.1f, 10f)
        speed shouldBe 0f
    }

    "should go with max speed on left" {
        val speed = AndroidPlayerMoveLogic.getPlayerSpeed(10f, 10f)
        speed shouldBe 10f
    }

    "should go with max speed on right" {
        val speed = AndroidPlayerMoveLogic.getPlayerSpeed(-10f, 10f)
        speed shouldBe 10f
    }

    "should go on right" {
        val speed = AndroidPlayerMoveLogic.getPlayerSpeed(-2f, 10f)
        assert(speed > 13 && speed < 14)
    }

    "should go on left" {
        val speed = AndroidPlayerMoveLogic.getPlayerSpeed(-2.5f, 10f)
        assert(speed > 11 && speed < 12)
    }
})