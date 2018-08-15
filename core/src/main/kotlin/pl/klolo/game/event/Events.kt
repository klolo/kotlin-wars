package pl.klolo.game.event

import pl.klolo.game.entity.Entity

sealed class Event()

object OnLeft : Event()
object OnRight : Event()
object OnSpace : Event()
class RegisterEntity() : Event() {
    var entity: Entity? = null
}