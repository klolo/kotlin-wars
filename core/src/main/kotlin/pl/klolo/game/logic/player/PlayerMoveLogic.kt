package pl.klolo.game.logic.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import pl.klolo.game.configuration.Profile
import pl.klolo.game.engine.GameEngine.Companion.applicationConfiguration
import pl.klolo.game.engine.ProfileHolder
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.*
import pl.klolo.game.extensions.execute

abstract class PlayerMoveLogic(private val eventProcessor: EventProcessor,
                               private val profileHolder: ProfileHolder) {

    enum class Direction { LEFT, RIGHT, NONE }

    private val playerSpeed = applicationConfiguration.getConfig("engine")
            .getDouble("playerSpeed")
            .toFloat()    // seconds per screen width

    private var currentMove: Action = execute {}
    private var direction = Direction.NONE
    private val margin = 50f

    protected fun SpriteEntityWithLogic.initializeMoving(): EventProcessor.Subscription {
        x = Gdx.graphics.width.toFloat() / 2 - width / 2

        if (profileHolder.activeProfile != Profile.DESKTOP) {
            return eventProcessor.subscribe(id)
        }

        return eventProcessor
                .subscribe(id)
                .onEvent(OnLeftDown) {
                    if (x - 100 > 0) {
                        direction = Direction.LEFT
                        onMove(x - Gdx.graphics.width.toFloat(), playerSpeed)
                    }
                }
                .onEvent(OnRightDown) {
                    if (x + 100 < Gdx.graphics.width.toFloat()) {
                        direction = Direction.RIGHT
                        onMove(x + Gdx.graphics.width.toFloat(), playerSpeed)
                    }
                }
                .onEvent(OnRightUp) {
                    direction = Direction.NONE
                    removeAction(currentMove)
                }
                .onEvent(OnLeftUp) {
                    direction = Direction.NONE
                    removeAction(currentMove)
                }
    }

    fun SpriteEntityWithLogic.checkPosition() {
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
            x = Gdx.graphics.width.toFloat()
        }

        if (profileHolder.activeProfile == Profile.ANDROID) {
            movingOnAndroid()
        }
    }

    private fun SpriteEntityWithLogic.movingOnAndroid() {
        val accelY = Gdx.input.accelerometerY;

        val accelerometrDetectionLevel = 0.5
        if (accelY < -1 * accelerometrDetectionLevel) {
            //go left
            direction = Direction.LEFT
            onMove(x - Gdx.graphics.width.toFloat(), playerSpeed - (playerSpeed * Math.min(1f, Math.abs(accelY))))
            return
        }

        if (accelY > +accelerometrDetectionLevel) {
            direction = Direction.RIGHT
            onMove(x + Gdx.graphics.width.toFloat(), playerSpeed - (playerSpeed * Math.min(1f, Math.abs(accelY))))
            return
        }

        direction = Direction.NONE
    }

    private fun SpriteEntityWithLogic.onMove(x: Float, playerSpeed: Float) {
        removeAction(currentMove)
        currentMove = moveTo(x, y, playerSpeed)
        addAction(currentMove)
    }

}