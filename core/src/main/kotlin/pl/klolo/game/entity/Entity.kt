package pl.klolo.game.entity

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch

interface Entity {
    fun draw(batch: Batch, camera: OrthographicCamera)
    fun update(delta: Float)
    fun dispose()
    val shouldBeRemove: Boolean
    val uniqueId: String
    val layer: Int
}