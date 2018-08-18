package pl.klolo.game

import box2dLight.ConeLight
import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.graphics.OrthographicCamera
import pl.klolo.game.extensions.toColor
import pl.klolo.game.physics.GamePhysics

class GameLighting(private val gamePhysics: GamePhysics) {
    private lateinit var rayHandler: RayHandler

    fun initLights() {
        RayHandler.useDiffuseLight(true)
        rayHandler = RayHandler(gamePhysics.world)
        rayHandler.setBlur(true)
        rayHandler.setBlurNum(3)
        rayHandler.setShadows(true)
        rayHandler.setAmbientLight("#222222".toColor())
    }

    fun dispose() {
        rayHandler.dispose()
    }

    fun render(camera: OrthographicCamera) {
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    fun createPointLight(rays: Int, color: String, distance: Float, x: Float, y: Float): PointLight {
        return PointLight(rayHandler, rays, color.toColor(), distance, x, y) // TODO: dispose
    }

    fun createDirectionalLight(rays: Int, color: String, distance: Float, x: Float, y: Float, directionDegree: Float, coneDegree: Float): ConeLight {
        return ConeLight(rayHandler, rays, color.toColor(), distance, x, y, directionDegree, coneDegree)
    }
}