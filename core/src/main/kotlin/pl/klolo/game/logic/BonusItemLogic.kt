package pl.klolo.game.logic

import pl.klolo.game.engine.GameLighting
import pl.klolo.game.engine.SoundEffect
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
        val eventProcessor: EventProcessor,
        gameLighting: GameLighting,
        gamePhysics: GamePhysics) : BaseBonusLogic(eventProcessor, gameLighting, gamePhysics) {

    override fun getEventToSendOnCollisionWithPlayer(): Event {
        eventProcessor.sendEvent(PlaySound(SoundEffect.YIPEE))
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

class DoublePointsBonusLogic(val eventProcessor: EventProcessor, gameLighting: GameLighting, gamePhysics: GamePhysics)
    : BaseBonusLogic(eventProcessor, gameLighting, gamePhysics) {

    override fun getEventToSendOnCollisionWithPlayer(): Event {
        return EnableDoublePoints
    }
}