package pl.klolo.game.logic.enemy

import box2dLight.Light
import com.badlogic.gdx.graphics.Color
import pl.klolo.game.configuration.Colors.red
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.common.executeAfterDelay
import java.util.*

class ExplosionEffect(private val gameLighting: GameLighting, private val distance: Float, private val lightColor: Color = red) {
    private val hitLights = Stack<Light>()

    var addLight: SpriteEntityWithLogic.() -> Unit = {
        hitLights.push(gameLighting.createPointLight(150, lightColor, distance, x + width / 2, y + height / 2))
        executeAfterDelay(0.15f) {
            hitLights.pop()?.remove()
        }
    }

    fun onDispose() {
        hitLights.forEach(Light::remove)
    }
}