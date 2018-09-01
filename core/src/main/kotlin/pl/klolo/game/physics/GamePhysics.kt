package pl.klolo.game.physics

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import pl.klolo.game.engine.GameEngine

class GamePhysics(private val contactListener: ContactListener) {
    lateinit var world: World
    private lateinit var debugRenderer: Box2DDebugRenderer
    private var bodyToRemove: List<Body> = mutableListOf()

    private val enableDebugRender: Boolean by lazy {
        GameEngine.applicationConfiguration
                .getConfig("engine")
                .getBoolean("debugRender")
    }

    fun initPhysics() {
        val gravityVec = Vector2(0f, -9.8f)
        world = World(gravityVec, true)
        world.setContactListener(contactListener)
        debugRenderer = Box2DDebugRenderer()
    }

    fun onDispose() {
        world.step(0f, 0, 0)
    }

    fun update() {
        world.step(1 / 60f, 6, 2)

        bodyToRemove.forEach { body ->
            body.isActive = false
            body.fixtureList.forEach { body.destroyFixture(it) }
            world.destroyBody(body)
        }

        bodyToRemove = mutableListOf()
    }

    fun debugRender(matrix: Matrix4) {
        if (enableDebugRender) {
            debugRenderer.render(world, matrix)
        }
    }

    fun dispose() {
        world.dispose()
    }

    fun destroy(body: Body) {
        bodyToRemove += body
    }

    fun createDynamicBody(bodyDef: BodyDef = BodyDef()): Body {
        bodyDef.apply {
            type = BodyDef.BodyType.DynamicBody
        }
        return world.createBody(bodyDef)
    }

    fun <T : Shape> getStandardFixtureDef(_shape: T): FixtureDef {
        return FixtureDef().apply {
            shape = _shape
            density = 0.5f
            friction = 0.4f
            restitution = 0.6f
        }
    }
}