package pl.klolo.game.logic.bonus

import com.badlogic.gdx.Gdx
import pl.klolo.game.entity.EntityConfiguration
import pl.klolo.game.entity.EntityLogic
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.createEntity
import pl.klolo.game.entity.kind.EntityWithLogic
import pl.klolo.game.entity.kind.SpriteEntityWithLogic
import pl.klolo.game.event.EnemyDestroyed
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity
import java.util.*


class ExtraStarBonusGenerator(
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<EntityWithLogic> {

    private lateinit var starConfiguration: EntityConfiguration

    override val initialize: EntityWithLogic.() -> Unit = {
        starConfiguration = entityRegistry.getConfigurationById("starBonus")

        eventProcessor
                .subscribe(id)
                .onEvent<EnemyDestroyed> {
                    val entity: SpriteEntityWithLogic = createEntityOnPosition(it.x, it.y)
                    entity.logic.apply { initialize.invoke(entity) }
                    eventProcessor.sendEvent(RegisterEntity(entity))
                }
    }

    private fun createEntityOnPosition(enemyXPosition: Float, enemyYPosition: Float): SpriteEntityWithLogic {
        return createEntity(starConfiguration, false) {
            x = enemyXPosition
            y = enemyYPosition
        } as SpriteEntityWithLogic
    }

    override val onUpdate: EntityWithLogic.(Float) -> Unit = {

    }

    override val onDispose: EntityWithLogic.() -> Unit = {

    }
}