package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.configuration.Profile
import pl.klolo.game.engine.ProfileHolder
import pl.klolo.game.entity.SpriteEntityWithLogic


class MenuOverlayLogic(private val profileHolder: ProfileHolder) : EntityLogic<SpriteEntityWithLogic> {

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {

    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()
        useLighting = false

        if (profileHolder.activeProfile == Profile.ANDROID) {
            rotation = 180f
            x = width
            y = height
            width = Gdx.graphics.width.toFloat()
            height = Gdx.graphics.width.toFloat()
        }

    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {

    }

}