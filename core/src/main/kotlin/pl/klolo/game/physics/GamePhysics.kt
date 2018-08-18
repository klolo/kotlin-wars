package pl.klolo.game.physics

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer

class GamePhysics(private val contactListener: ContactListener) {
    lateinit var world: World
    private lateinit var debugRenderer: Box2DDebugRenderer

    fun initPhysics() {
        val gravityVec = Vector2(0f, -9.8f)
        world = World(gravityVec, true)
        world.setContactListener(contactListener)
        debugRenderer = Box2DDebugRenderer()
    }

    fun update() {
        world.step(1 / 60f, 6, 2);
    }

    fun debugRender(matrix: Matrix4) {
        debugRenderer.render(world, matrix)
    }

    fun dispose() {
        world.dispose()

    }

    fun destroy(body: Body) {
        world.destroyBody(body)
    }

    fun createBody(bodyDef: BodyDef): Body {
        return world.createBody(bodyDef)
    }
}