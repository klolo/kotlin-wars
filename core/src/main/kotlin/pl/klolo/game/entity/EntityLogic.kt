package pl.klolo.game.entity

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch

interface EntityLogic<T : Entity> {
    val initialize: T.() -> Unit
    val onUpdate: T.(Float) -> Unit
    val onDispose: T.() -> Unit
}

interface EntityLogicWithRendering<T : Entity> : EntityLogic<T> {
    val draw: T.(batch: Batch, camera: OrthographicCamera) -> Unit
}