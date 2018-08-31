package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.entity.SpriteEntityWithLogic


class MenuOverlayLogic() : EntityLogic<SpriteEntityWithLogic> {

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {

    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()
        useLighting = false
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {

    }

}