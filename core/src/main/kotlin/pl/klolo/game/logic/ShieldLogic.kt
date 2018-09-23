package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import pl.klolo.game.configuration.Colors.blueLight
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.engine.SoundEffect
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.*
import pl.klolo.game.entity.EntityLogic
import pl.klolo.game.logic.enemy.ExplosionEffect
import pl.klolo.game.physics.GamePhysics

class ShieldLogic(
        private val gamePhysics: GamePhysics,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {

    private var explosionLights = ExplosionEffect(gameLighting, 200f, blueLight)
    private lateinit var physicsShape: CircleShape
    private lateinit var body: Body

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        useLighting = false

        eventProcessor
                .subscribe(id)
                .onEvent<PlayerChangePosition> {
                    x = it.x - width / 2
                    y = it.y - height / 2
                }
                .onEvent<DisableShield> {
                    Gdx.app.debug(this.javaClass.name, "disabling shield...")
                    body.isActive = false
                    display = false
                }
                .onEvent<EnableShield> {
                    Gdx.app.debug(this.javaClass.name, "enabling shield...")
                    body.isActive = true
                    display = true
                }
                .onEvent<LaserHitInShield> {
                    if (!body.isActive) {
                        return@onEvent
                    }

                    eventProcessor.sendEvent(PlaySound(SoundEffect.SHIELD_COLLISION))
                    explosionLights.addLightOnPosition(this, it.x, it.y)
                }

        createShieldPhysics()
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        body.setTransform(x + width / 2, y + height / 2, 0.0f)
        explosionLights.updateLight()
    }

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        physicsShape.dispose()
        gamePhysics.destroy(body)
        explosionLights.onDispose()
    }

    private fun SpriteEntityWithLogic.createShieldPhysics() {
        body = gamePhysics.createDynamicBody()
        body.isActive = false

        physicsShape = CircleShape().apply { radius = width / 2 }
        display = false

        val fixture = body.createFixture(gamePhysics.getStandardFixtureDef(physicsShape))
        fixture.userData = this
    }
}