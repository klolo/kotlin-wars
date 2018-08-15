package pl.klolo.game

import org.springframework.context.support.GenericApplicationContext
import pl.klolo.game.configuration.Profile
import pl.klolo.game.configuration.beanDefinition

val applicationContext = GenericApplicationContext()

fun createGameEngine(profile: Profile): GameEngine {
    applicationContext
            .apply {
                environment.setActiveProfiles(profile.name)
                refresh()
            }
    beanDefinition.initialize(applicationContext)
    return applicationContext.getBean(GameEngine::class.java)
}