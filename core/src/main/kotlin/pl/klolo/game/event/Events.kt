package pl.klolo.game.event

import pl.klolo.game.entity.Entity

sealed class Event()

object OnLeft : Event()
object OnRight : Event()
object OnSpace : Event()
object EnemyDestroyed: Event()

class OnCollision(val entity: Entity? = null): Event()

class RegisterEntity(val entity: Entity? = null) : Event()