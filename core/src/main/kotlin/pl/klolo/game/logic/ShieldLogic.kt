package pl.klolo.game.logic

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import pl.klolo.game.configuration.Colors.blueLight
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.DisableShield
import pl.klolo.game.event.EnableShield
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.LaserHitInShield
import pl.klolo.game.logic.helper.ExplosionLights
import pl.klolo.game.logic.player.PlayerMoveLogic
import pl.klolo.game.physics.GamePhysics

class ShieldLogic(
        private val gamePhysics: GamePhysics,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic>, PlayerMoveLogic(eventProcessor) {

    private var explosionLights = ExplosionLights(gameLighting, 200f, blueLight)
    private lateinit var physicsShape: CircleShape
    lateinit var body: Body

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        useLighting = false
        y = height * 1.2f

        initializeMoving()
                .onEvent(DisableShield) {
                    println("disabling shield...")
                    body.isActive = false
                    display = false
                }
                .onEvent(EnableShield) {
                    println("enabling shield...")
                    body.isActive = true
                    display = true
                }
                .onEvent(LaserHitInShield) {
                    if (!body.isActive) {
                        return@onEvent
                    }

                    explosionLights.addLight(this)
                }

        body = gamePhysics.createDynamicBody()
        body.isActive = false

        physicsShape = CircleShape().apply { radius = width / 2 }
        display = false

        val fixture = body.createFixture(gamePhysics.getStandardFixtureDef(physicsShape))
        fixture?.userData = this
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        body.setTransform(x + width / 2, y, 0.0f)
        checkPosition()
    }

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        physicsShape.dispose()
        gamePhysics.destroy(body)
        explosionLights.onDispose()
    }
}