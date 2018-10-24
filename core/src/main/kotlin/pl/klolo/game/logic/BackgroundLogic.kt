package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import pl.klolo.game.engine.Profile
import pl.klolo.game.engine.ProfileHolder
import pl.klolo.game.engine.assetManager
import pl.klolo.game.entity.kind.SpriteWithCustomRendering
import pl.klolo.game.entity.EntityLogicWithRendering

class BackgroundLogic(private val profileHolder: ProfileHolder) : EntityLogicWithRendering<SpriteWithCustomRendering> {
    companion object {
        @JvmStatic
        private lateinit var firstBackground: Sprite

        @JvmStatic
        private lateinit var secondBackground: Sprite

        @JvmStatic
        private lateinit var thirdBackground: Sprite

        private var positionInitialized = false
    }

    private val movingSpeed = 20
    private val isPortrait = profileHolder.activeProfile == Profile.ANDROID

    override val onDispose: SpriteWithCustomRendering.() -> Unit = {
    }

    override val initialize: SpriteWithCustomRendering.() -> Unit = {
        Gdx.app.debug(this.javaClass.name, "createSubscription")

        val imageToLoad = if (isPortrait) "assets/background-portrait.jpg" else entityConfiguration.file

        if (!positionInitialized) {
            val texture = assetManager.get(imageToLoad, Texture::class.java)
            firstBackground = Sprite(texture)

            if (isPortrait) {
                firstBackground.y = Gdx.graphics.height.toFloat()
            }
            else {
                firstBackground.x = Gdx.graphics.width.toFloat() * -1
            }

            secondBackground = Sprite(texture)
            thirdBackground = Sprite(texture)

            if (isPortrait) {
                thirdBackground.y = Gdx.graphics.height.toFloat() * -1
            }
            else {
                thirdBackground.x = Gdx.graphics.width.toFloat()
            }

            positionInitialized = true
        }
    }

    override val draw: SpriteWithCustomRendering.(batch: Batch, camera: OrthographicCamera) -> Unit =
            { batch: Batch, _ ->
                batch.disableBlending() // for performance purpose when drawing big texture
                batch.draw(firstBackground, firstBackground.x, firstBackground.y, originX, originY, width, height, scaleX, scaleY, rotation)
                batch.draw(secondBackground, secondBackground.x, secondBackground.y, originX, originY, width, height, scaleX, scaleY, rotation)
                batch.draw(thirdBackground, thirdBackground.x, thirdBackground.y, originX, originY, width, height, scaleX, scaleY, rotation)
                batch.enableBlending()
            }

    override val onUpdate: SpriteWithCustomRendering.(Float) -> Unit = {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()

        if (isPortrait) {
            firstBackground.y += it * movingSpeed
            secondBackground.y += it * movingSpeed
            thirdBackground.y += it * movingSpeed

            if (firstBackground.y > Gdx.graphics.height.toFloat() * 1.5) {
                val temp = firstBackground
                firstBackground = secondBackground
                secondBackground = thirdBackground
                temp.y = thirdBackground.y - Gdx.graphics.height.toFloat()
                thirdBackground = temp
            }
        }
        else {
            firstBackground.x -= it * movingSpeed
            secondBackground.x -= it * movingSpeed
            thirdBackground.x -= it * movingSpeed

            if (firstBackground.x < Gdx.graphics.width.toFloat() * -1.5) {
                val temp = firstBackground
                firstBackground = secondBackground
                secondBackground = thirdBackground
                temp.x = thirdBackground.x + Gdx.graphics.width.toFloat()
                thirdBackground = temp
            }
        }
    }
}