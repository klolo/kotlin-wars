package pl.klolo.game.logic

import pl.klolo.game.entity.Entity

interface EntityLogic<T : Entity> {
    val initialize: T.() -> Unit
    val onUpdate: T.(Float) -> Unit
}

class EmptyLogic<T : Entity> : EntityLogic<T> {
    override val initialize: T.() -> Unit = {

    }

    override val onUpdate: T.(Float) -> Unit = {

    }
}