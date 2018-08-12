package pl.klolo.game.entity

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch

data class Bounds(
        var x: Float,
        var y: Float,
        var width: Float,
        var height: Float) {

    constructor(init: Bounds.() -> Unit) : this(0f, 0f, 0f, 0f) {
        this.init()
    }
}

interface Entity {
    fun draw(batch: Batch, camera: OrthographicCamera)
    fun update(delta: Float)
    fun dispose()
    val uniqueId: String
    val bounds: Bounds
}