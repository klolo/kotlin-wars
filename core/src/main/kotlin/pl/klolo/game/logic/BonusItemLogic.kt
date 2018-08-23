package pl.klolo.game.logic

import pl.klolo.game.engine.GameLighting
import pl.klolo.game.event.*
import pl.klolo.game.physics.GamePhysics

class AdditionalLifeBonusLogic(
        eventProcessor: EventProcessor,
        gameLighting: GameLighting,
        gamePhysics: GamePhysics) : BaseBonusLogic(eventProcessor, gameLighting, gamePhysics) {

    override fun getEventToSendOnCollisionWithPlayer(): Event {
        return AddPlayerLife(20)
    }
}

class AdditionalPointsBonusLogic(
        eventProcessor: EventProcessor,
        gameLighting: GameLighting,
        gamePhysics: GamePhysics) : BaseBonusLogic(eventProcessor, gameLighting, gamePhysics) {

    override fun getEventToSendOnCollisionWithPlayer(): Event {
        return AddPoints(100)
    }
}

class SuperBulletBonusLogic(eventProcessor: EventProcessor, gameLighting: GameLighting, gamePhysics: GamePhysics)
    : BaseBonusLogic(eventProcessor, gameLighting, gamePhysics) {

    override fun getEventToSendOnCollisionWithPlayer(): Event {
        return EnableSuperBullet
    }
}

class ShieldBonusLogic(eventProcessor: EventProcessor, gameLighting: GameLighting, gamePhysics: GamePhysics)
    : BaseBonusLogic(eventProcessor, gameLighting, gamePhysics) {

    override fun getEventToSendOnCollisionWithPlayer(): Event {
        return EnableShield
    }
}

class DoublePointsBonusLogic(eventProcessor: EventProcessor, gameLighting: GameLighting, gamePhysics: GamePhysics)
    : BaseBonusLogic(eventProcessor, gameLighting, gamePhysics) {

    override fun getEventToSendOnCollisionWithPlayer(): Event {
        return EnableDoublePoints
    }
}