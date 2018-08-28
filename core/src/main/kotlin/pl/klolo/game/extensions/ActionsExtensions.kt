package pl.klolo.game.extensions

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction

fun execute(callback: () -> Unit): RunnableAction {
    return Actions.run(callback)
}

fun Actor.addSequence(vararg actions: Action) {
    addAction(sequence(*actions))
}

fun Actor.addForeverSequence(vararg actions: Action) {
    addAction(forever(sequence(*actions)))
}

fun Actor.executeAfterDelay(delay: Float, callback: () -> Unit) {
    addSequence(Actions.delay(delay), Actions.run(callback))
}