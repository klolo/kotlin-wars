package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import pl.klolo.game.applicationContext
import pl.klolo.game.entity.*
import pl.klolo.game.event.EnemyDestroyed
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity
import java.util.*

class EnemyBaseLogic(
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<EntityWithLogic> {

    private val maxEnemiesOnStage = 3
    private var enemiesCount = 0

    override val onDispose: EntityWithLogic.() -> Unit = {

    }

    override val initialize: EntityWithLogic.() -> Unit = {
        eventProcessor
                .subscribe(id)
                .onEvent(EnemyDestroyed) {
                    enemiesCount--
                }

        addAction(Actions.forever(
                Actions.sequence(
                        Actions.run {
                            if (enemiesCount < maxEnemiesOnStage) {
                                val laserConfiguration = entityRegistry.getConfigurationById("enemyRed" + Random().nextInt(5))
                                if (laserConfiguration != null) {
                                    enemiesCount++
                                    createEnemy(laserConfiguration)
                                }
                            }
                        },
                        Actions.delay(1f)
                )
        ))
    }

    private fun EntityWithLogic.createEnemy(laserConfiguration: EntityConfiguration) {
        val random = Random()
        val bulletXPosition = random.nextInt(Gdx.graphics.width.toFloat().toInt())
        val bulletYPosition = Gdx.graphics.height.toFloat() + 10

        val bulletEntity: SpriteEntityWithLogic = createEntity(laserConfiguration, applicationContext) {
            x = bulletXPosition.toFloat()
            y = bulletYPosition
        } as SpriteEntityWithLogic

        eventProcessor.sendEvent(RegisterEntity(bulletEntity))
    }


    override val onUpdate: EntityWithLogic.(Float) -> Unit = {
    }

}