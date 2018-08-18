package pl.klolo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.beust.klaxon.Klaxon
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import pl.klolo.game.entity.Entity
import pl.klolo.game.entity.EntityConfiguration
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity

class Stage(
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor) : Entity, ApplicationContextAware {
    override val layer: Int = -1
    override val id: Int = -1
    override val shouldBeRemove: Boolean = false
    lateinit var _applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext?) {
        _applicationContext = applicationContext!!
    }

    override val uniqueName: String = "main-stage"
    private var entities = emptyList<Entity>()

    fun initEntities() {
        val json = Gdx.files.internal("stage1-entities.json").readString()
        val entitiesConfiguration = Klaxon().parseArray<EntityConfiguration>(json) ?: emptyList()

        entityRegistry.addConfiguration(entitiesConfiguration)

        entities = entitiesConfiguration
                .map { createEntity(it, _applicationContext) }
                .filter { it.layer >= 0 }
                .sortedBy { it.layer }

        eventProcessor
                .subscribe(id)
                .onEvent(RegisterEntity::class.java) { event: RegisterEntity ->
                    val newEntity = event.entity
                    if (newEntity != null) {
                        println("Register entity: ${newEntity.uniqueName}")
                        entities += newEntity
                    }

                }
    }

    override fun update(delta: Float) {
        entities
                .filter { it.shouldBeRemove }
                .forEach {
                    it.dispose()
                }

        entities = entities.filter { true != it.shouldBeRemove }
        entities.forEach {
            it.update(delta)
        }
    }

    override fun dispose() {
        entities.forEach {
            it.dispose()
        }
    }

    override fun draw(batch: Batch, camera: OrthographicCamera) {
        entities.forEach {
            it.draw(batch, camera)
        }

        batch.end()
    }

}