package pl.klolo.game.engine

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import pl.klolo.game.event.*

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
            Input.Keys.LEFT -> eventProcessor.sendEvent(OnLeftUp)
            Input.Keys.RIGHT -> eventProcessor.sendEvent(OnRightUp)
            Input.Keys.SPACE -> eventProcessor.sendEvent(OnSpace)
            Input.Keys.DOWN -> eventProcessor.sendEvent(OnArrowDown)
            Input.Keys.UP -> eventProcessor.sendEvent(OnArrowUp)
            Input.Keys.ENTER -> eventProcessor.sendEvent(OnEnter)
            Input.Keys.ESCAPE -> eventProcessor.sendEvent(OnEscape)
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT -> eventProcessor.sendEvent(OnLeftDown)
            Input.Keys.RIGHT -> eventProcessor.sendEvent(OnRightDown)
        }
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

}