package pl.klolo.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World

class GamePhysics {
    lateinit var world: World

    fun initPhysics() {
        val gravityVec = Vector2(0f, -9.8f)
        world = World(gravityVec, true)
    }

    fun update() {
        world.step(1 / 60f, 6, 2);
    }

    fun dispose() {
        world.dispose()
    }

}