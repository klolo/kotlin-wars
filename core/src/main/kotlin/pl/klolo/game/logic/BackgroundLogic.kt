package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import pl.klolo.game.entity.EntityWithLogic
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.entity.SpriteWithCustomRendering

class BackgroundLogic() : EntityLogicWithRendering<SpriteWithCustomRendering> {
    private lateinit var leftBackground: Sprite
    private lateinit var centerBackground: Sprite
    private lateinit var rightBackground: Sprite

    private val movingSpeed = 10

    override val onDispose: SpriteWithCustomRendering.() -> Unit = {

    }

    override val initialize: SpriteWithCustomRendering.() -> Unit = {
        leftBackground = Sprite(Texture(Gdx.files.internal(entityConfiguration.image)))
        leftBackground.x = Gdx.graphics.width.toFloat() * -1

        centerBackground = Sprite(Texture(Gdx.files.internal(entityConfiguration.image)))
        centerBackground.x = 0f

        rightBackground = Sprite(Texture(Gdx.files.internal(entityConfiguration.image)))
        rightBackground.x = Gdx.graphics.width.toFloat()
    }

    override val draw: SpriteWithCustomRendering.(batch: Batch, camera: OrthographicCamera) -> Unit =
            { batch: Batch, camera: OrthographicCamera ->
                batch.draw(leftBackground, leftBackground.x, y, originX, originY, width, height, scaleX, scaleY, rotation)
                batch.draw(centerBackground, centerBackground.x, y, originX, originY, width, height, scaleX, scaleY, rotation)
                batch.draw(rightBackground, rightBackground.x, y, originX, originY, width, height, scaleX, scaleY, rotation)
            }

    override val onUpdate: SpriteWithCustomRendering.(Float) -> Unit = {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()

        leftBackground.x -= it * movingSpeed
        centerBackground.x -= it * movingSpeed
        rightBackground.x -= it * movingSpeed

        if (leftBackground.x < Gdx.graphics.width.toFloat() * -1.5) {
            val temp = leftBackground
            leftBackground = centerBackground
            centerBackground = rightBackground
            temp.x = rightBackground.x + Gdx.graphics.width.toFloat()
            rightBackground = temp
        }
    }
}