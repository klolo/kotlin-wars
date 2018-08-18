package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import pl.klolo.game.GameLighting
import pl.klolo.game.physics.GamePhysics
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.extensions.execute

class BulletLogic(
        private val gamePhysics: GamePhysics,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {

    private var bulletLight: PointLight? = null
    private var physicsShape: PolygonShape? = null
    private lateinit var body: Body

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        physicsShape?.dispose()
        gamePhysics.destroy(body)
        bulletLight?.remove()
    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        bulletLight = gameLighting.createPointLight(100, "#9adde3ff", 50f, x, y)

        createPhysics()

        addAction(sequence(
                moveTo(x, y + Gdx.graphics.height.toFloat(), 3f),
                execute(Runnable { shouldBeRemove = true })
        ))
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        bulletLight?.setPosition(x + width / 2, y + height / 2)
        body.setTransform(x + width / 2, y + height / 2, 0.0f)
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        val bodyDef = BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
        }

        physicsShape = PolygonShape()
        physicsShape?.setAsBox(width / 2, height / 2);

        val fixtureDef = FixtureDef().apply {
            shape = physicsShape
            density = 0.5f
            friction = 0.4f
            restitution = 0.6f
        }

        body = gamePhysics.createBody(bodyDef)
        val fixture = body.createFixture(fixtureDef)
        fixture?.userData = this
    }
}