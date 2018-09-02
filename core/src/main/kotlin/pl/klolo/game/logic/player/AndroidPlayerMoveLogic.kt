package pl.klolo.game.logic.player

import com.badlogic.gdx.Gdx
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.PlayerChangePosition

class AndroidPlayerMoveLogic(private val eventProcessor: EventProcessor) : PlayerMoveLogic, BasePlayerMove(eventProcessor) {
    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        x = Gdx.graphics.width.toFloat() / 2 - width / 2
    }

    override val createSubscription: SpriteEntityWithLogic.() -> EventProcessor.Subscription = { eventProcessor.subscribe(id) }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        super.checkBoundPosition(this)
        move(Gdx.input.accelerometerX)
        eventProcessor.sendEvent(PlayerChangePosition(x + width / 2, y + height / 2))
    }

    private fun SpriteEntityWithLogic.move(accelerometerX: Float) {
        val playerSpeed = getPlayerSpeed(accelerometerX, playerSpeed)

        if (playerSpeed == 0f) {
            direction = Direction.NONE
            removeAction(currentMove)
            return
        }

        if (accelerometerX < 0) {
            direction = Direction.RIGHT
            onMove(x + Gdx.graphics.width.toFloat(), playerSpeed)
        }
        else {
            direction = Direction.LEFT
            onMove(x - Gdx.graphics.width.toFloat(), playerSpeed)
        }
    }

    companion object {
        fun getPlayerSpeed(accelerometerX: Float, speed: Float): Float {
            val detectionLevel = 1f
            val maxDetectionLevel = 6f
            val detectionRange = maxDetectionLevel - detectionLevel
            val absX = Math.abs(accelerometerX);

            if (absX < detectionLevel) {
                return 0f
            }

            if (absX >= maxDetectionLevel) {
                return speed
            }

            return 2 * speed - (absX / detectionRange) * speed
        }
    }

}