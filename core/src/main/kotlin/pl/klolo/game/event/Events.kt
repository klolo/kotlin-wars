package pl.klolo.game.event

import pl.klolo.game.entity.Entity

sealed class Event()

object OnLeftUp : Event()
object OnRightUp : Event()
object OnLeftDown : Event()
object OnRightDown : Event()
object OnSpace : Event()
object EnemyDestroyed : Event()

class OnCollision(val entity: Entity? = null) : Event()
class AddPoints(val points: Int = 0) : Event()

class RegisterEntity(val entity: Entity? = null) : Event()