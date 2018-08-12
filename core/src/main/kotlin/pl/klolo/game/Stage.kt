package pl.klolo.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import pl.klolo.game.entity.Bounds
import pl.klolo.game.entity.Entity
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.logic.createPlayerConfiguration

class Stage(private val eventProcessor: EventProcessor) : Entity {
    override val bounds: Bounds
        get() = TODO("not implemented")

    override val uniqueId: String = ""
    private var entities = emptyList<Entity>()

    fun loadStage() {
        entities += createPlayerConfiguration(eventProcessor) // TODO: dynamiczne ladowani obiektow
    }

    override fun update(delta: Float) {
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
    }

}