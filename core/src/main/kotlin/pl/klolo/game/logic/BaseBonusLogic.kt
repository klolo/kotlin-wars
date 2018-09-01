package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import pl.klolo.game.configuration.Colors
import pl.klolo.game.engine.*
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.Event
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.OnCollision
import pl.klolo.game.common.addForeverSequence
import pl.klolo.game.common.addSequence
import pl.klolo.game.common.execute
import pl.klolo.game.entity.EntityLogic
import pl.klolo.game.physics.GamePhysics

abstract class BaseBonusLogic(
        private val profileHolder: ProfileHolder,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting,
        private val gamePhysics: GamePhysics) : EntityLogic<SpriteEntityWithLogic> {

    private lateinit var light: PointLight
    private lateinit var physicsShape: CircleShape
    private lateinit var body: Body
    private var ignoreNextCollision = false
    private val bonusSpeed = 15f

    abstract fun getEventToSendOnCollisionWithPlayer(): Event;

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        light = gameLighting.createPointLight(150, Colors.gold, 90f, x, y)

        createPhysics()
        addForeverSequence(
                scaleTo(1.4f, 1.4f, 1f, Interpolation.linear),
                scaleTo(1f, 1f, 1f, Interpolation.linear)
        )

        addSequence(
                moveTo(x, -1 * height, 18f),
                execute {
                    shouldBeRemove = true
                }
        )

        eventProcessor.subscribe(id)
                .onEvent(OnCollision::class.java) {
                    val collidedEntity = it.entity!!

                    if (isPlayerByName(collidedEntity) && !ignoreNextCollision) {
                        ignoreNextCollision = true
                        clearActions()
                        addSequence(
                                scaleTo(0.01f, 0.01f, 0.2f, Interpolation.linear),
                                execute {
                                    eventProcessor.sendEvent(getEventToSendOnCollisionWithPlayer())
                                    shouldBeRemove = true
                                }
                        )
                    }
                }
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        light.setPosition(x + width / 2, y + height / 2)
        body.setTransform(x + width / 2, y + height / 2, 0.0f)
    }

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        light.remove()
        physicsShape.dispose()
        gamePhysics.destroy(body)
        clearActions()
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        body = gamePhysics.createDynamicBody()
        physicsShape = CircleShape().apply { radius = width / 2 }
        val fixture = body.createFixture(gamePhysics.getStandardFixtureDef(physicsShape))
        fixture?.userData = this
    }
}
