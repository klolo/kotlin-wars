package pl.klolo.game.logic.player.move

import com.badlogic.gdx.Gdx
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.*

class DesktopPlayerMoveLogic(private val eventProcessor: EventProcessor) : PlayerMoveLogic, BasePlayerMove(eventProcessor) {

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        x = Gdx.graphics.width.toFloat() / 2 - width / 2
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        super.checkBoundPosition(this)
        eventProcessor.sendEvent(PlayerChangePosition(x + width / 2, y + height / 2))
    }
    override val createSubscription: SpriteEntityWithLogic.() -> EventProcessor.Subscription = {
        eventProcessor
                .subscribe(id)
                .onEvent<PressedLeftDown> {
                    if (x - width > 0) {
                        direction = Direction.LEFT
                        onMove(x - Gdx.graphics.width.toFloat(), playerSpeed)
                    }
                }
                .onEvent<PressedRightDown> {
                    if (x + width < Gdx.graphics.width.toFloat()) {
                        direction = Direction.RIGHT
                        onMove(x + Gdx.graphics.width.toFloat(), playerSpeed)
                    }
                }
                .onEvent<PressedRightUp> {
                    direction = Direction.NONE
                    removeAction(currentMove)
                }
                .onEvent<PressedLeftUp> {
                    direction = Direction.NONE
                    removeAction(currentMove)
                }
    }
}