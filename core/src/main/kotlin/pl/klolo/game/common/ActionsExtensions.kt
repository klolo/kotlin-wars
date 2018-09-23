package pl.klolo.game.common

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction

fun execute(callback: () -> Unit): RunnableAction {
    return Actions.run(callback)
}

fun Actor.addSequence(vararg actions: Action): Action {
    val sequenceAction = sequence(*actions)
    addAction(sequenceAction)
    return sequenceAction
}

fun Actor.addForeverSequence(vararg actions: Action): Action {
    val repeatAction = forever(sequence(*actions))
    addAction(repeatAction)
    return repeatAction
}

fun Actor.executeAfterDelay(delay: Float, callback: () -> Unit): Action {
    return addSequence(Actions.delay(delay), Actions.run(callback))
}