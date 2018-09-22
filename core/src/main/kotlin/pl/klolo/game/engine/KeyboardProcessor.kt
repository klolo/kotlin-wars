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
            Input.Keys.LEFT -> eventProcessor.sendEvent(PressedLeftUp)
            Input.Keys.RIGHT -> eventProcessor.sendEvent(PressedRightUp)
            Input.Keys.SPACE -> eventProcessor.sendEvent(PressedSpace)
            Input.Keys.DOWN -> eventProcessor.sendEvent(PressedArrowDown)
            Input.Keys.UP -> eventProcessor.sendEvent(PressedArrowUp)
            Input.Keys.ENTER -> eventProcessor.sendEvent(PressedEnter)
            Input.Keys.ESCAPE -> eventProcessor.sendEvent(PressedEscape)
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT -> eventProcessor.sendEvent(PressedLeftDown)
            Input.Keys.RIGHT -> eventProcessor.sendEvent(PressedRightDown)
        }
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

}