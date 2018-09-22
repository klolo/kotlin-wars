package pl.klolo.game.physics

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import pl.klolo.game.entity.Entity
import pl.klolo.game.entity.SpriteEntityWithLogic
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.OnCollision

class ContactListener(private val eventProcessor: EventProcessor) : ContactListener {
    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        if (contact?.fixtureB?.userData != null && contact.fixtureA?.userData != null) {
            val userDataA = contact.fixtureA.userData as Entity
            val userDataB = contact.fixtureB.userData as Entity

            if (userDataA is SpriteEntityWithLogic && userDataB is SpriteEntityWithLogic) {
                val collisionPoint = contact.worldManifold.points[0]
                eventProcessor.sendEvent(OnCollision(userDataA, collisionPoint.x, collisionPoint.y), userDataB.id)
                eventProcessor.sendEvent(OnCollision(userDataB, collisionPoint.x, collisionPoint.y), userDataA.id)
            }
        }
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}

    override fun beginContact(contact: Contact?) {}

    override fun endContact(contact: Contact?) {}

}