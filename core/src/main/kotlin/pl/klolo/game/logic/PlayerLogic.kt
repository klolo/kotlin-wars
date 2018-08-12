package pl.klolo.game.logic

import com.badlogic.gdx.graphics.Texture
import pl.klolo.game.entity.Bounds
import pl.klolo.game.entity.TextureEntity
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.MoveLeft
import pl.klolo.game.event.MoveRight
import java.util.*

fun createPlayerConfiguration(eventProcessor: EventProcessor): TextureEntity {
    val playerLogic = PlayerLogic(eventProcessor)

    return TextureEntity {
        uniqueId = "Player"
        logic = playerLogic
        texture = Texture("badlogic.jpg")
        bounds = Bounds {
            x = Random().nextInt(440) + 0f
            y = Random().nextInt(250) + 0f
            width = 100f
            height = 100f
        }
    }.apply(playerLogic.initialize)
}


class PlayerLogic(val eventProcessor: EventProcessor) : EntityLogic<TextureEntity> {

    override val initialize: TextureEntity.() -> Unit = {
        eventProcessor
                .subscribe(uniqueId)
                .onEvent(MoveLeft) {
                    bounds.x -= 10
                }
                .onEvent(MoveRight) {
                    bounds.x += 10
                }
    }

    override val onUpdate: TextureEntity.(Float) -> Unit = {
        bounds.y++
    }
}