package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import pl.klolo.game.GameLighting
import pl.klolo.game.physics.GamePhysics
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.EnemyDestroyed
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.OnCollision
import pl.klolo.game.extensions.execute

class EnemyLogic(
        private val gamePhysics: GamePhysics,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {
    private var light: PointLight? = null
    private var physicsShape: CircleShape? = null
    private lateinit var body: Body

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        light?.remove()
        physicsShape?.dispose()
        gamePhysics.destroy(body)
    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        light = gameLighting.createPointLight(50, "#ff1111", 100f, x, y)
        createPhysics()

        addAction(sequence(
                moveTo(x, y - Gdx.graphics.width - 100, 20f),
                execute(Runnable { onDestroy() })
        ))

        eventProcessor.subscribe(id)
                .onEvent(OnCollision::class.java) {
                    val collidedEntity = it.entity
                    if (collidedEntity != null) {
                        if (collidedEntity.uniqueName.contains("laser")) {
                            onDestroy()
                        }
                    }
                }
    }

    fun SpriteEntityWithLogic.onDestroy() {
        shouldBeRemove = true
        eventProcessor.sendEvent(EnemyDestroyed)
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        light?.setPosition(x + width / 2, y + height)
        body?.setTransform(x + width / 2, y + height / 2, 0.0f)
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        val bodyDef = BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
        }

        physicsShape = CircleShape().apply { radius = width / 2 }

        val fixtureDef = FixtureDef().apply {
            shape = physicsShape
            density = 0.5f
            friction = 0.4f
            restitution = 0.6f
        }

        body = gamePhysics.createBody(bodyDef)
        val fixture = body?.createFixture(fixtureDef)
        fixture?.userData = this
    }

}