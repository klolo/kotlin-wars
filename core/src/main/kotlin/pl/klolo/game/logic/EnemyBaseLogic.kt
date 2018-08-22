package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.entity.*
import pl.klolo.game.event.EnemyDestroyed
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity
import java.util.*

val speedOfTheDecreasingEnemyShootDelayPerCreatedEnemy = 250f

class EnemyBaseLogic(
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<EntityWithLogic> {

    private var maxEnemiesOnStage = 3
    private var enemiesCount = 0
    private var totalCreatedEnemy = 0

    override val onDispose: EntityWithLogic.() -> Unit = {

    }

    override val initialize: EntityWithLogic.() -> Unit = {
        println("EnemyBaseLogic creating...")
        eventProcessor
                .subscribe(id)
                .onEvent(EnemyDestroyed) {
                    println("Enemy destoyed. Total enemies: $totalCreatedEnemy Max enemies: $maxEnemiesOnStage, shoot delay: ${3f - (totalCreatedEnemy / speedOfTheDecreasingEnemyShootDelayPerCreatedEnemy)}")
                    enemiesCount--
                }

        addAction(Actions.forever(
                Actions.sequence(
                        Actions.run {
                            if (enemiesCount < maxEnemiesOnStage) {
                                val laserConfiguration = entityRegistry.getConfigurationById("enemyRed" + (1 + Random().nextInt(5)))
                                enemiesCount++
                                totalCreatedEnemy++
                                createEnemy(laserConfiguration)

                                maxEnemiesOnStage = Math.max(Math.floorDiv(totalCreatedEnemy, 10), 3)
                            }
                        },
                        Actions.delay(1f)
                )
        ))
    }

    private fun EntityWithLogic.createEnemy(laserConfiguration: EntityConfiguration) {
        val random = Random()
        val margin = 100

        val enemyXPosition = random.nextInt(Gdx.graphics.width.toFloat().toInt() - margin) + margin
        val enemyYPosition = Gdx.graphics.height.toFloat() + margin

        val enemyEntity: SpriteEntityWithLogic = createEntity(laserConfiguration, applicationContext, false) {
            x = enemyXPosition.toFloat()
            y = enemyYPosition
        } as SpriteEntityWithLogic

        (enemyEntity.logic as EnemyLogic).apply {
            shootDelay = 3f - (totalCreatedEnemy / speedOfTheDecreasingEnemyShootDelayPerCreatedEnemy)

        }
        enemyEntity.logic.apply { initialize.invoke(enemyEntity) }
        eventProcessor.sendEvent(RegisterEntity(enemyEntity))
    }

    override val onUpdate: EntityWithLogic.(Float) -> Unit = {
    }

}