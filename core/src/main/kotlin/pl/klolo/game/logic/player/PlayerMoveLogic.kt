package pl.klolo.game.logic.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.*
import pl.klolo.game.extensions.execute

abstract class PlayerMoveLogic(private val eventProcessor: EventProcessor) {

    private val playerSpeed = 3f // seconds per screen width
    private var currentMove: Action = execute {}

    protected fun SpriteEntityWithLogic.initializeMoving(): EventProcessor.Subscription {
        x = Gdx.graphics.width.toFloat() / 2 - width / 2

        return eventProcessor
                .subscribe(id)
                .onEvent(OnLeftDown) {
                    if (x - 100 > 0) {
                        onMove(x - Gdx.graphics.width.toFloat(), playerSpeed)
                    }
                }
                .onEvent(OnRightDown) {
                    if (x + 100 < Gdx.graphics.width.toFloat()) {
                        onMove(x + Gdx.graphics.width.toFloat(), playerSpeed)
                    }
                }
                .onEvent(OnRightUp) {
                    removeAction(currentMove)
                }
                .onEvent(OnLeftUp) {
                    removeAction(currentMove)
                }
    }

    private fun SpriteEntityWithLogic.onMove(x: Float, playerSpeed: Float) {
        removeAction(currentMove)
        currentMove = Actions.moveTo(x, y, playerSpeed)
        addAction(currentMove)
    }
}