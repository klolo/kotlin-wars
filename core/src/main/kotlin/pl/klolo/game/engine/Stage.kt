package pl.klolo.game.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.beust.klaxon.Klaxon
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
        private val soundManager: SoundManager,
        private val gamePhysics: GamePhysics,
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor) {

    private var entities = emptyList<Entity>()

    fun initEntities() {
        Gdx.app.debug(this.javaClass.name, "initialize")

        subscribe()
        loadStage("assets/menu-entities.json")
    }

    private fun subscribe() {
        eventProcessor
                .subscribe(-1)
                .onEvent(RegisterEntity::class.java) { event: RegisterEntity ->
                    val newEntity = event.entity
                    if (newEntity != null) {
                        entities += newEntity
                        Gdx.app.debug(this.javaClass.name, "register entity uniqueName = ${newEntity.uniqueName}. Total bodies: ${gamePhysics.world.bodyCount}")
                    }
                }
                .onEvent(GameOver::class.java) {
                    Gdx.app.debug(this.javaClass.name, "game over")
                    switchStage("assets/gameover-menu-entities.json")
                    soundManager.playSong(Song.MENU)
                }
                .onEvent(StartNewGame) {
                    Gdx.app.debug(this.javaClass.name, "start new game")
                    switchStage("assets/game-entities.json")
                    soundManager.playSong(Song.GAME)
                }
    }

    private fun switchStage(nextStageFileConfiguration: String) {
        disposeCurrentStageEntities()
        subscribe()
        loadStage(nextStageFileConfiguration)
        soundManager.initialize()
    }

    private fun disposeCurrentStageEntities() {
        eventProcessor.clearAllSubscription()
        gamePhysics.onDispose()

        Gdx.app.debug(this.javaClass.name, "clearing entities")
        entities.forEach { it.dispose() }
        entities = emptyList()
    }

    private fun loadStage(stageConfigurationFilename: String) {
        val json = Gdx.files.internal(stageConfigurationFilename).readString()
        val entitiesConfiguration = Klaxon().parseArray<EntityConfiguration>(json) ?: emptyList()

        entityRegistry.addConfiguration(entitiesConfiguration)
        val loadedEntities = entitiesConfiguration
                .filter { it.initOnCreate }
                .map { createEntity<Entity>(it) }

        entities += loadedEntities
        entities = entities.sortedBy { it.layer }
        Gdx.app.debug(this.javaClass.name, "entities loaded: ${entities.joinToString { it.uniqueName }}")
    }

    fun update(delta: Float) {
        entities
                .filter { it.shouldBeRemove }
                .forEach {
                    Gdx.app.debug(this.javaClass.name, "dispose entity: $it")
                    it.dispose()
                }

        entities = entities.filter { !it.shouldBeRemove }
        entities.forEach {
            it.update(delta)
        }
    }

    fun dispose() {
        entities.forEach(Entity::dispose)
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