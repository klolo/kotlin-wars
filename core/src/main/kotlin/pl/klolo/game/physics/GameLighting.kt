package pl.klolo.game.physics

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import pl.klolo.game.common.Colors
import pl.klolo.game.engine.GameEngine

class GameLighting(private val gamePhysics: GamePhysics) {
    private lateinit var rayHandler: RayHandler
    private var entityScaleFactor: Float = 1.0f

    fun initLights() {
        entityScaleFactor = GameEngine.applicationConfiguration.getConfig("engine")
                .getDouble("entityScaleFactor")
                .toFloat()

        RayHandler.useDiffuseLight(true)
        rayHandler = RayHandler(gamePhysics.world)
        rayHandler.setBlur(true)
        rayHandler.setBlurNum(3)
        rayHandler.setShadows(true)
        rayHandler.setAmbientLight(Colors.ambient)
    }

    fun dispose() {
        rayHandler.dispose()
    }

    fun clearLights() {
        rayHandler.removeAll()
    }

    fun render(camera: OrthographicCamera) {
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    fun createPointLight(rays: Int, color: Color, distance: Float, x: Float, y: Float): PointLight {
        return PointLight(rayHandler, rays, color, distance * entityScaleFactor, x, y)
    }
}