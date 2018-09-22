package pl.klolo.game.event

import pl.klolo.game.engine.SoundEffect
import pl.klolo.game.entity.Entity

sealed class Event()

// keyboard
object PressedLeftUp : Event()

object PressedRightUp : Event()
object PressedLeftDown : Event()
object PressedRightDown : Event()
object PressedSpace : Event()
object PressedArrowDown : Event()
object PressedArrowUp : Event()
object PressedEnter : Event()
object PressedEscape : Event()

// player
object EnemyDestroyed : Event()

class PlayerChangePosition(val x: Float = 0f, val y: Float = 0f) : Event()
class ChangePlayerLfeLevel(val actualPlayerLifeLevel: Int = 100) : Event()
class AddPoints(val points: Int = 0) : Event()
class AddPlayerLife(val lifeAmount: Int = 0) : Event()

object EnableSuperBullet : Event()
object DisableSuperBullet : Event()

object EnableShield : Event()
object DisableShield : Event()

object EnableDoublePoints : Event()
object DisableDoublePoints : Event()

class LaserHitInShield(val x: Float = 0f, val y: Float = 0f) : Event()


// engine
class RegisterEntity(val entity: Entity? = null) : Event()

object GameOver : Event()
object StartNewGame : Event()
object OpenMainMenu : Event()

class Collision(
        val entity: Entity? = null,  // collided object
        val x: Float = 0f,  // collision position
        val y: Float = 0f) : Event() // collision position

class PlaySound(val soundEffect: SoundEffect? = null) : Event()
object StopMusic : Event()