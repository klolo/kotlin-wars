package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import pl.klolo.game.GameLighting
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.EventProcessor

class BulletLogic(
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {

    private lateinit var bulletLight: PointLight

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        bulletLight = gameLighting.createPointLight(100, "#9adde3ff", 90f, x, y)

        addAction(Actions.sequence(
                Actions.moveTo(x, y + 1000, 3f),
                Actions.run {
                    shouldBeRemove = true
                    bulletLight.remove()
                })
        )
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        bulletLight.setPosition(x + width / 2, y + height / 2)
    }
}