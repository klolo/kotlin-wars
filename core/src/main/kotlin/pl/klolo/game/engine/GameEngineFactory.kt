package pl.klolo.game.engine

import pl.klolo.game.configuration.Profile
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.physics.ContactListener
import pl.klolo.game.physics.GamePhysics

val gameDependencyInjectionContext = GameDependencyInjectionContext()
private lateinit var filepathResolver: FilepathResolver

fun resolveFilepath(path: String): String = filepathResolver.resolve(path)

fun createGameEngine(profile: Profile): GameEngine {
    filepathResolver = FilepathResolver(profile)
    gameDependencyInjectionContext
            .apply {
                registerBean(EntityRegistry::class.java)
                registerBean(EventProcessor::class.java)

                registerInputKeyboardProcessorDependOnProfile(profile)

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

fun GameDependencyInjectionContext.registerInputKeyboardProcessorDependOnProfile(profile: Profile) {
    when (profile) {
        Profile.DESKTOP, Profile.WEB -> registerBean(KeyboardProcessor::class.java)
        Profile.ANDROID -> {
        }
    }
}