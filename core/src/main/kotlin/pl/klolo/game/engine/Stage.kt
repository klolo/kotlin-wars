package pl.klolo.game.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.beust.klaxon.Klaxon
import pl.klolo.game.entity.*
import pl.klolo.game.event.*
import pl.klolo.game.physics.GamePhysics

class Stage(
        private val gameLighting: GameLighting,
        private val soundManager: SoundManager,
        private val gamePhysics: GamePhysics,
        private val entityRegistry: EntityRegistry,
        private val eventProcessor: EventProcessor) {

    private var entities = emptyList<Entity>()

    fun initEntities() {
        Gdx.app.debug(this.javaClass.name, "createSubscription")

        subscribe()
        loadStage("assets/entities/loader-entities.json")
    }

    private fun subscribe() {
        eventProcessor
                .subscribe(-1)
                .onEvent(OpenMainMenu) {
                    switchStage("assets/entities/menu-entities.json")
                }
                .onEvent(RegisterEntity::class) { event: RegisterEntity ->
                    val newEntity = event.entity
                    if (newEntity != null) {
                        entities += newEntity
                        Gdx.app.debug(this.javaClass.name, "register entity uniqueName = ${newEntity.uniqueName}. Total bodies: ${gamePhysics.world.bodyCount}")
                    }
                }
                .onEvent(GameOver) {
                    Gdx.app.debug(this.javaClass.name, "game over")
                    switchStage("assets/entities/gameover-menu-entities.json")
                    soundManager.playSong(Song.MENU)
                }
                .onEvent(StartNewGame) {
                    Gdx.app.debug(this.javaClass.name, "start new game")
                    switchStage("assets/entities/game-entities.json")
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

        gameLighting.clearLights()
    }

    private fun loadStage(stageConfigurationFilename: String) {
        val entityScaleFactor = GameEngine.applicationConfiguration.getConfig("engine")
                .getDouble("entityScaleFactor")
                .toFloat()

        val json = Gdx.files.internal(stageConfigurationFilename).readString()

        val entitiesConfiguration = Klaxon()
                .parseArray<EntityConfiguration>(json)
                ?.map { scaleEntity(it, entityScaleFactor) } ?: emptyList()

        entityRegistry.addConfiguration(entitiesConfiguration)
        val loadedEntities = entitiesConfiguration
                .filter { it.initOnCreate }
                .map { createEntity<Entity>(it) }

        entities += loadedEntities
        entities = entities.sortedBy { it.layer }
        Gdx.app.debug(this.javaClass.name, "entities loaded: ${entities.joinToString { it.uniqueName }}")
    }

    private fun scaleEntity(entityConfiguration: EntityConfiguration, entityScaleFactor: Float): EntityConfiguration {
        return EntityConfiguration(
                uniqueName = entityConfiguration.uniqueName,
                type = entityConfiguration.type,
                logicClass = entityConfiguration.logicClass,
                file = entityConfiguration.file,
                x = entityConfiguration.x,
                y = entityConfiguration.y,
                width = entityConfiguration.width * entityScaleFactor,
                height = entityConfiguration.height * entityScaleFactor,
                layer = entityConfiguration.layer,
                initOnCreate = entityConfiguration.initOnCreate,
                useLighting = entityConfiguration.useLighting
        )
    }

    fun update(delta: Float) {
        entities
                .filter { it.shouldBeRemove }
                .forEach {
                    Gdx.app.debug(this.javaClass.name, "dispose entity: ${it.uniqueName}")
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