package pl.klolo.game.event

import pl.klolo.game.entity.Entity

sealed class Event()

object OnLeftUp : Event()
object OnRightUp : Event()
object OnLeftDown : Event()
object OnRightDown : Event()
object OnSpace : Event()
object OnArrowDown : Event()
object OnArrowUp : Event()
object OnEnter : Event()
object OnEscape : Event()
object EnemyDestroyed : Event()

class PlayerHit(val actualPlayerLifeLevel: Int = 100) : Event()
class OnCollision(val entity: Entity? = null) : Event()
class AddPoints(val points: Int = 0) : Event()

class GameOver(val totalPoints: Int = 0) : Event()
object StartNewGame : Event()

class RegisterEntity(val entity: Entity? = null) : Event()