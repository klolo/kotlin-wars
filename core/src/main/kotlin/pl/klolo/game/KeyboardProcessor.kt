package pl.klolo.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.OnLeft
import pl.klolo.game.event.OnRight
import pl.klolo.game.event.OnSpace

/**
 * TODO: Przeniesc do desktop. Klasa zalezna od platformy gdzie jest uruchamiana gra.
 */
class KeyboardProcessor(private val eventProcessor: EventProcessor) : InputProcessor {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT -> eventProcessor.sendEvent(OnLeft)
            Input.Keys.RIGHT -> eventProcessor.sendEvent(OnRight)
            Input.Keys.SPACE -> eventProcessor.sendEvent(OnSpace)
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

}