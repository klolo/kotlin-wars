package pl.klolo.game.logic.helper

import box2dLight.Light
import pl.klolo.game.configuration.Colors
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.extensions.executeAfterDelay
import java.util.*

class ExplosionLights(private val gameLighting: GameLighting, private val distance: Float) {
    private val hitLights = Stack<Light>()

    var addLight: SpriteEntityWithLogic.() -> Unit = {
        hitLights.push(gameLighting.createPointLight(150, Colors.red, distance, x + width / 2, y + height / 2))
        executeAfterDelay(0.15f) { hitLights.pop()?.remove() }
    }

    fun onDispose() {
        hitLights.forEach(Light::remove)
    }
}