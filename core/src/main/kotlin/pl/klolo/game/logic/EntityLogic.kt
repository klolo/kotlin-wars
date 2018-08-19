package pl.klolo.game.logic

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import pl.klolo.game.entity.Entity

interface EntityLogic<T : Entity> {
    val initialize: T.() -> Unit
    val onUpdate: T.(Float) -> Unit
    val onDispose: T.() -> Unit
}

interface EntityLogicWithRendering<T : Entity> : EntityLogic<T> {
    val draw: T.(batch: Batch, camera: OrthographicCamera) -> Unit
}

class EmptyLogic<T : Entity> : EntityLogic<T> {
    override val initialize: T.() -> Unit = {

    }

    override val onDispose: T.() -> Unit = {

    }

    override val onUpdate: T.(Float) -> Unit = {

    }
}