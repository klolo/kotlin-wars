package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import pl.klolo.game.entity.SpriteWithCustomRendering

class BackgroundLogic() : EntityLogicWithRendering<SpriteWithCustomRendering> {
    companion object {
        @JvmStatic
        private lateinit var leftBackground: Sprite

        @JvmStatic
        private lateinit var centerBackground: Sprite

        @JvmStatic
        private lateinit var rightBackground: Sprite

        private var positionInitialized = false
    }

    private val movingSpeed = 10

    override val onDispose: SpriteWithCustomRendering.() -> Unit = {
    }

    override val initialize: SpriteWithCustomRendering.() -> Unit = {
        Gdx.app.debug(this.javaClass.name,"createSubscription")

        if (!positionInitialized) {
            val texture = Texture(Gdx.files.internal(entityConfiguration.image))
            leftBackground = Sprite(texture)
            leftBackground.x = Gdx.graphics.width.toFloat() * -1

            centerBackground = Sprite(texture)
            centerBackground.x = 0f

            rightBackground = Sprite(texture)
            rightBackground.x = Gdx.graphics.width.toFloat()
            positionInitialized = true
        }
    }

    override val draw: SpriteWithCustomRendering.(batch: Batch, camera: OrthographicCamera) -> Unit =
            { batch: Batch, _ ->
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