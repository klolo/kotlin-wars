package pl.klolo.game.logic

import box2dLight.Light
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import pl.klolo.game.configuration.Colors.red
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.DisableShield
import pl.klolo.game.event.EnableShield
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.LaserHitInShield
import pl.klolo.game.extensions.executeAfterDelay
import pl.klolo.game.logic.player.PlayerMoveLogic
import pl.klolo.game.physics.GamePhysics
import java.util.*


class ShieldLogic(
        private val gamePhysics: GamePhysics,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic>, PlayerMoveLogic(eventProcessor) {

    private val hitLights = Stack<Light>()
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

                    println("enemy laser hit in shield. Available lights: ${hitLights.size}")

                    hitLights.push(gameLighting.createPointLight(150, red, 120f, x, y))
                    executeAfterDelay(0.15f) { hitLights.pop()?.remove() }
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
        hitLights.forEach { light -> light.setPosition(x + width / 2, y + height / 2) }
    }

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        physicsShape.dispose()
        gamePhysics.destroy(body)
    }
}