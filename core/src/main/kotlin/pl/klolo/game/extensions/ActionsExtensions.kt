package pl.klolo.game.extensions

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.action
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction

fun execute(callback: () -> Unit): RunnableAction {
    return Actions.run(callback)
}

fun executeAfterDelay(delay: Float, callback: () -> Unit): Action {
    return Actions.sequence(
            Actions.delay(delay),
            Actions.run(callback)
    )
}