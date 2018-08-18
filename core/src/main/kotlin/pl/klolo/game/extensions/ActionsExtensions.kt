package pl.klolo.game.extensions

import com.badlogic.gdx.scenes.scene2d.actions.Actions.action
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction

fun execute(runnable: Runnable): RunnableAction {
    val action = action<RunnableAction>(RunnableAction::class.java)
    action.runnable = runnable
    return action
}