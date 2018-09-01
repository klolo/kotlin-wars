package pl.klolo.game.logic.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import pl.klolo.game.configuration.Profile
import pl.klolo.game.engine.GameEngine.Companion.applicationConfiguration
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.extensions.execute


enum class Direction { LEFT, RIGHT, NONE }

interface PlayerMoveLogic {
    val initialize: SpriteEntityWithLogic.() -> Unit
    val onUpdate: SpriteEntityWithLogic.(Float) -> Unit
    val createSubscription: SpriteEntityWithLogic.() -> EventProcessor.Subscription
}

fun getMoveLogicImplementation(profile: Profile, eventProcessor: EventProcessor): PlayerMoveLogic {
    return when (profile) {
        Profile.ANDROID -> AndroidPlayerMoveLogic(eventProcessor)
        else -> DesktopPlayerMoveLogic(eventProcessor)
    }
}

abstract class BasePlayerMove(private val eventProcessor: EventProcessor) {

    protected val playerSpeed = applicationConfiguration.getConfig("engine")
            .getDouble("playerSpeed")
            .toFloat()    // seconds per screen width

    protected var currentMove: Action = execute {}
    protected var direction = Direction.NONE
    protected val margin = 50f

    protected val checkBoundPosition: SpriteEntityWithLogic.() -> Unit = {
        val centerX = x + width / 2
        val stopMovingLeft = centerX < margin && direction == Direction.LEFT
        val stopMovingRight = centerX > Gdx.graphics.width.toFloat() - margin && direction == Direction.RIGHT

        if (stopMovingLeft || stopMovingRight) {
            removeAction(currentMove)
            direction = Direction.NONE
        }

        if (x < 0) {
            x = 0f
        }

        if (x > Gdx.graphics.width.toFloat()) {
            x = Gdx.graphics.width.toFloat() - width
        }
    }

    protected fun SpriteEntityWithLogic.onMove(x: Float, playerSpeed: Float) {
        removeAction(currentMove)
        currentMove = moveTo(x, y, playerSpeed)
        addAction(currentMove)
    }

}