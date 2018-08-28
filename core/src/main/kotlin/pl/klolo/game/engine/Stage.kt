package pl.klolo.game.engine

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
import pl.klolo.game.event.GameOver
import pl.klolo.game.event.RegisterEntity
import pl.klolo.game.event.StartNewGame
import pl.klolo.game.physics.GamePhysics

class Stage(
        private val gamePhysics: GamePhysics,
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor) : ApplicationContextAware {
    lateinit var _applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext?) {
        _applicationContext = applicationContext!!
    }

    private var entities = emptyList<Entity>()

    fun initEntities() {
        subscribe()
        loadStage("menu-entities.json")
    }

    private fun subscribe() {
        eventProcessor
                .subscribe(-1)
                .onEvent(RegisterEntity::class.java) { event: RegisterEntity ->
                    val newEntity = event.entity
                    if (newEntity != null) {
                        entities += newEntity
                        println("create entity ${newEntity.uniqueName}. Total bodies: ${gamePhysics.world.bodyCount}")
                    }
                }
                .onEvent(GameOver::class.java) {
                    println(" -- GAME OVER ---")
                    switchStage("gameover-menu-entities.json")
                }
                .onEvent(StartNewGame) {
                    println(" -- START GAME ---")
                    switchStage("game-entities.json")
                }
    }

    private fun switchStage(nextStageFileConfiguration: String) {
        disposeCurrentStageEntities()
        subscribe()
        loadStage(nextStageFileConfiguration)
    }

    private fun disposeCurrentStageEntities() {
        eventProcessor.clearAllSubscription()
        gamePhysics.onDispose()
        entities.forEach { it.dispose() }
        entities = emptyList()
    }

    private fun loadStage(stageConfigurationFilename: String) {
        val json = Gdx.files.internal(stageConfigurationFilename).readString()
        val entitiesConfiguration = Klaxon().parseArray<EntityConfiguration>(json) ?: emptyList()

        entityRegistry.addConfiguration(entitiesConfiguration)
        entities += entitiesConfiguration
                .filter { it.initOnCreate }
                .map { createEntity<Entity>(it, _applicationContext) }
                .sortedBy { it.layer }
    }

    fun update(delta: Float) {
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

    fun dispose() {
        entities.forEach {
            it.dispose()
        }
    }

    fun drawWithLight(batch: Batch, camera: OrthographicCamera) {
        entities
                .filter { it.useLighting }
                .forEach {
                    it.draw(batch, camera)
                }
    }

    fun drawWithoutLight(batch: Batch, camera: OrthographicCamera) {
        entities
                .filter { !it.useLighting }
                .forEach {
                    it.draw(batch, camera)
                }
    }

}