package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import pl.klolo.game.configuration.Colors.red
import pl.klolo.game.entity.SpriteWithCustomRendering
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.ChangePlayerLfeLevel

class PlayerLifeBarLogic(private val eventProcessor: EventProcessor) : EntityLogicWithRendering<SpriteWithCustomRendering> {
    private lateinit var fill: Sprite
    private lateinit var background: Sprite

    private var lifeAmount = 1f

    override val onDispose: SpriteWithCustomRendering.() -> Unit = {

    }

    override val initialize: SpriteWithCustomRendering.() -> Unit = {
        Gdx.app.debug(this.javaClass.name, "createSubscription")

        fill = Sprite(Texture(Gdx.files.internal(entityConfiguration.image)))
        background = Sprite(Texture(Gdx.files.internal("assets/lifebar.png")))

        useLighting = false
        width = entityConfiguration.width
        height = entityConfiguration.height

        eventProcessor
                .subscribe(id)
                .onEvent(ChangePlayerLfeLevel::class.java) {
                    lifeAmount = it.actualPlayerLifeLevel / 100f
                }
    }

    override val draw: SpriteWithCustomRendering.(batch: Batch, camera: OrthographicCamera) -> Unit =
            { batch: Batch, _ ->
                val batchColor = batch.color
                batch.color = getLifebarColor()
                batch.draw(background, x, y, originX, originY, entityConfiguration.width, height, scaleX, scaleY, rotation)
                batch.draw(fill,
                        x + (entityConfiguration.width * 0.05f),
                        y + (entityConfiguration.height * 0.2f),
                        originX, originY, width, height * 0.6f, scaleX, scaleY, rotation)
                batch.color = batchColor
            }

    private fun getLifebarColor(): Color {
        return Color(0.9f, lifeAmount, lifeAmount, 1f)
    }

    override val onUpdate: SpriteWithCustomRendering.(Float) -> Unit = {
        x = Gdx.graphics.width.toFloat() - entityConfiguration.width * 1.2f
        width = Math.max(0f, (entityConfiguration.width * 0.9f) * lifeAmount)
        y = Gdx.graphics.height.toFloat() - height * 1.4f
    }
}