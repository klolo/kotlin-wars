package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import pl.klolo.game.engine.assetManager
import pl.klolo.game.entity.EntityLogicWithRendering
import pl.klolo.game.entity.SpriteWithCustomRendering


class MenuOverlayLogic() : EntityLogicWithRendering<SpriteWithCustomRendering> {

    private lateinit var leftTop: Sprite
    private lateinit var rightBottom: Sprite
    private lateinit var rightTop: Sprite
    private lateinit var middleBottom: Sprite
    private lateinit var clouds: Sprite

    override val onDispose: SpriteWithCustomRendering.() -> Unit = {

    }

    override val initialize: SpriteWithCustomRendering.() -> Unit = {
        leftTop = Sprite(assetManager.get("assets/left_top_menu.png", Texture::class.java))
        leftTop.setPosition(0f, Gdx.graphics.height.toFloat() - leftTop.texture.height)

        rightBottom = Sprite(assetManager.get("assets/right_bottom_menu.png", Texture::class.java))
        rightBottom.setPosition(Gdx.graphics.width.toFloat() - rightBottom.texture.width, 0f)

        rightTop = Sprite(assetManager.get("assets/right_top_menu.png", Texture::class.java))
        rightTop.setPosition(Gdx.graphics.width.toFloat() - rightTop.texture.width, Gdx.graphics.height.toFloat() - rightTop.texture.height)

        middleBottom = Sprite(assetManager.get("assets/middle_bottom.png", Texture::class.java))
        middleBottom.setPosition(0f, 0f)

        clouds = Sprite(assetManager.get("assets/menu_cloud.png", Texture::class.java))
        clouds.setPosition(0f, 0f)
        clouds.setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        useLighting = false
    }

    override val onUpdate: SpriteWithCustomRendering.(Float) -> Unit = {

    }

    override val draw: SpriteWithCustomRendering.(batch: Batch, camera: OrthographicCamera) -> Unit = { batch, camera ->
        clouds.draw(batch)
        leftTop.draw(batch)
        rightBottom.draw(batch)
        rightTop.draw(batch)
        middleBottom.draw(batch)
    }

}