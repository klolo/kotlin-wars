package pl.klolo.game.event

import pl.klolo.game.engine.SoundEffect
import pl.klolo.game.entity.Entity

sealed class Event()

// keyboard
object OnLeftUp : Event()

object OnRightUp : Event()
object OnLeftDown : Event()
object OnRightDown : Event()
object OnSpace : Event()
object OnArrowDown : Event()
object OnArrowUp : Event()
object OnEnter : Event()
object OnEscape : Event()

// player
object EnemyDestroyed : Event()

class ChangePlayerLfeLevel(val actualPlayerLifeLevel: Int = 100) : Event()
class AddPoints(val points: Int = 0) : Event()
class AddPlayerLife(val lifeAmount: Int = 0) : Event()
object EnableSuperBullet : Event()
object EnableShield : Event()
object DisableShield : Event()
object EnableDoublePoints : Event()
object LaserHitInShield : Event()


// engine
class RegisterEntity(val entity: Entity? = null) : Event()

class GameOver(val totalPoints: Int = 0) : Event()
object StartNewGame : Event()
class OnCollision(val entity: Entity? = null) : Event()
class PlaySound(val soundEffect: SoundEffect? = null) : Event()