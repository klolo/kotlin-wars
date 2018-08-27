package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import pl.klolo.game.configuration.Colors
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.engine.isPlayerByName
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.Event
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.OnCollision
import pl.klolo.game.extensions.execute
import pl.klolo.game.physics.GamePhysics

abstract class BaseBonusLogic(
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting,
        private val gamePhysics: GamePhysics) : EntityLogic<SpriteEntityWithLogic> {

    private lateinit var light: PointLight
    private lateinit var physicsShape: CircleShape
    private lateinit var body: Body
    private var ignoreNextCollision = false

    abstract fun getEventToSendOnCollisionWithPlayer(): Event;

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        light = gameLighting.createPointLight(150, Colors.gold, 90f, x, y)

        createPhysics()

        val moveAction = sequence(
                parallel(
                        moveTo(x, y - Gdx.graphics.width - 100, 20f),
                        forever(
                                sequence(
                                        scaleTo(1.3f, 1.3f, 1f, Interpolation.linear),
                                        scaleTo(0.9f, 0.9f, 1f, Interpolation.linear)
                                )
                        )

                ),
                execute { shouldBeRemove = true }
        )
        addAction(moveAction)

        eventProcessor.subscribe(id)
                .onEvent(OnCollision::class.java) {
                    val collidedEntity = it.entity!!

                    if (isPlayerByName(collidedEntity) && !ignoreNextCollision) {
                        ignoreNextCollision = true
                        clearActions()
                        addAction(
                                sequence(
                                        parallel(
                                                scaleTo(0.01f, 0.01f, 0.2f, Interpolation.linear)
                                        ),
                                        execute {
                                            eventProcessor.sendEvent(getEventToSendOnCollisionWithPlayer())
                                            shouldBeRemove = true
                                        }
                                )
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
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        body = gamePhysics.createDynamicBody()
        physicsShape = CircleShape().apply { radius = width / 2 }

        val fixture = body.createFixture(gamePhysics.getStandardFixtureDef(physicsShape))
        fixture?.userData = this
    }
}
