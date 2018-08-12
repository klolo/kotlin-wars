package pl.klolo.game.event

import javax.swing.text.html.parser.Entity

sealed class Event

object MoveLeft : Event()
object MoveRight : Event()
class CollisionDetect(val collidedEntity: Entity) : Event()