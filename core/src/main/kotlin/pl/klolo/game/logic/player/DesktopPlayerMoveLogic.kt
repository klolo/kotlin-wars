package pl.klolo.game.logic.player

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
                .onEvent(OnLeftDown) {
                    if (x - width > 0) {
                        direction = Direction.LEFT
                        onMove(x - Gdx.graphics.width.toFloat(), playerSpeed)
                    }
                }
                .onEvent(OnRightDown) {
                    if (x + width < Gdx.graphics.width.toFloat()) {
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
}