package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import pl.klolo.game.engine.GameLighting
import pl.klolo.game.engine.Highscore
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.engine.isEnemyLaser
import pl.klolo.game.entity.EntityConfiguration
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.*
import pl.klolo.game.extensions.execute
import pl.klolo.game.physics.GamePhysics

class PlayerLogic(
        private val highscore: Highscore,
        private val gamePhysics: GamePhysics,
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor,
        private val gameLighting: GameLighting) : EntityLogic<SpriteEntityWithLogic> {

    private var currentMove: Action = execute(Runnable {})
    private lateinit var playerLight: PointLight

    private lateinit var physicsShape: PolygonShape
    private lateinit var body: Body

    private var lifeLevel = 100
    private var points = 0

    override val onDispose: SpriteEntityWithLogic.() -> Unit = {
        physicsShape.dispose()
        playerLight.remove()
        gamePhysics.destroy(body)
    }

    override val initialize: SpriteEntityWithLogic.() -> Unit = {
        x = Gdx.graphics.width.toFloat() / 2 - width / 2
        playerLight = gameLighting.createPointLight(100, "#9adde3ff", 80f, x, y)

        val playerSpeed = 3f // seconds per screen width
        createPhysics()
        eventProcessor
                .subscribe(id)
                .onEvent(OnLeftDown) {
                    if (x - 100 > 0) {
                        removeAction(currentMove)
                        currentMove = moveTo(x - Gdx.graphics.width.toFloat(), y, playerSpeed)
                        addAction(currentMove)
                    }
                }
                .onEvent(OnRightDown) {
                    if (x + 100 < Gdx.graphics.width.toFloat()) {
                        removeAction(currentMove)
                        currentMove = moveTo(x + Gdx.graphics.width.toFloat(), y, playerSpeed)
                        addAction(currentMove)
                    }
                }
                .onEvent(OnRightUp) {
                    removeAction(currentMove)
                }
                .onEvent(OnLeftUp) {
                    removeAction(currentMove)
                }
                .onEvent(OnSpace) {
                    val laserConfiguration = entityRegistry.getConfigurationById("laserBlue01")
                    shootOnPosition(laserConfiguration)
                }
                .onEvent(AddPoints::class.java) {
                    points += it.points
                }
                .onEvent(OnCollision::class.java) {
                    val collidedEntity = it.entity!!
                    if (isEnemyLaser(collidedEntity)) {
                        lifeLevel -= 50
                        eventProcessor.sendEvent(PlayerHit(lifeLevel))

                        if (lifeLevel <= 0) {
                            highscore.addScore(points)
                            eventProcessor.sendEvent(GameOver(lifeLevel))
                        }
                    }
                }
    }

    private fun SpriteEntityWithLogic.shootOnPosition(laserConfiguration: EntityConfiguration) {
        val bulletXPosition = x + width / 2
        val bulletYPosition = y + height / 2

        val bulletEntity: SpriteEntityWithLogic = createEntity(laserConfiguration, applicationContext) {
            x = bulletXPosition
            y = bulletYPosition

        } as SpriteEntityWithLogic

        (bulletEntity.logic as BulletLogic).isEnemyBullet = false

        eventProcessor.sendEvent(RegisterEntity(bulletEntity))
    }

    override val onUpdate: SpriteEntityWithLogic.(Float) -> Unit = {
        playerLight.setPosition(x + width / 2, y + height / 2)
        body.setTransform(x + width / 2, y + height / 2, 0.0f)
    }

    private fun SpriteEntityWithLogic.createPhysics() {
        physicsShape = PolygonShape()
        physicsShape.setAsBox(width / 2, height / 2);
        body = gamePhysics.createDynamicBody()

        val fixture = body.createFixture(gamePhysics.getStandardFixtureDef(physicsShape))
        fixture?.userData = this
    }
}