package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import pl.klolo.game.configuration.Profile
import pl.klolo.game.engine.ProfileHolder
import pl.klolo.game.engine.assetManager
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.entity.EntityLogic


class MenuOverlayLogic(private val profileHolder: ProfileHolder) : EntityLogic<SpriteEntityWithLogic> {

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {

    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()
        useLighting = false

        if (profileHolder.activeProfile == Profile.ANDROID) {
            sprite = Sprite(assetManager.get("assets/menu-overlay-portrait.png", Texture::class.java))

        }

    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {

    }

}