package pl.klolo.game.engine

import com.badlogic.gdx.assets.AssetManager
import pl.klolo.game.configuration.Profile
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.physics.ContactListener
import pl.klolo.game.physics.GamePhysics

val gameDependencyInjectionContext = GameDependencyInjectionContext()
val assetManager = AssetManager()

class ProfileHolder(val activeProfile: Profile)

fun createGameEngine(profile: Profile): GameEngine {
    gameDependencyInjectionContext
            .apply {
                registerBean(EntityRegistry::class.java)
                registerBean(ProfileHolder(profile))
                registerBean(EventProcessor::class.java)
                registerBean(Highscore::class.java)
                registerBean(SoundManager::class.java)
                registerBean(ContactListener::class.java)
                registerBean(GamePhysics::class.java)
                registerBean(GameLighting::class.java)
                registerBean(Stage::class.java)
                registerBean(GameEngine::class.java)
            }

    return gameDependencyInjectionContext.getBeanByClass(GameEngine::class.java) as GameEngine
}