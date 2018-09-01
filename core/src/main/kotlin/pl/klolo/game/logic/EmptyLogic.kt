package pl.klolo.game.logic

import pl.klolo.game.entity.Entity
import pl.klolo.game.entity.EntityLogic

class EmptyLogic<T : Entity> : EntityLogic<T> {
    override val initialize: T.() -> Unit = {

    }

    override val onDispose: T.() -> Unit = {

    }

    override val onUpdate: T.(Float) -> Unit = {

    }
}