package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import pl.klolo.game.engine.Highscore
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.entity.*
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity
import java.util.*

class BonusLogic(
        private val highscore: Highscore,
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<EntityWithLogic> {
    private val random = Random()

    private val items by lazy {
        listOf(
                entityRegistry.getConfigurationById("medicineBonus") to 2,
                entityRegistry.getConfigurationById("starBonus") to 2,
                entityRegistry.getConfigurationById("superBulletBonus") to 2,
                entityRegistry.getConfigurationById("shieldBonus") to 2,
                entityRegistry.getConfigurationById("doublePointsBonus") to 2
        )
    }

    override val initialize: EntityWithLogic.() -> Unit = {
        println("BonusLogic creating...")

        addAction(forever(
                sequence(
                        Actions.run {
                            val randomItem = getRandomItemConfiguration()
                            if (shouldCreateItem(randomItem.second)) {
                                createItem(randomItem.first)
                            }
                        },
                        delay(1f)
                )
        ))
    }

    override val onDispose: EntityWithLogic.() -> Unit = {
    }

    override val onUpdate: EntityWithLogic.(Float) -> Unit = {
    }

    private fun getRandomItemConfiguration(): Pair<EntityConfiguration, Int> {
        return items[random.nextInt(items.size)]
    }

    private fun shouldCreateItem(createItemPropability: Int): Boolean {
        return random.nextInt(100) % createItemPropability == 0
    }

    private fun createItem(bonusItemConfiguration: EntityConfiguration) {
        println("creating bonus: ${bonusItemConfiguration.uniqueName}")
        val random = Random()
        val margin = 100

        val enemyXPosition = random.nextInt(Gdx.graphics.width.toFloat().toInt() - margin) + margin
        val enemyYPosition = Gdx.graphics.height.toFloat() + margin

        val enemyEntity: SpriteEntityWithLogic = createEntity(bonusItemConfiguration, applicationContext) {
            x = enemyXPosition.toFloat()
            y = enemyYPosition
        } as SpriteEntityWithLogic

        enemyEntity.logic.apply { initialize.invoke(enemyEntity) }
        eventProcessor.sendEvent(RegisterEntity(enemyEntity))
    }
}
