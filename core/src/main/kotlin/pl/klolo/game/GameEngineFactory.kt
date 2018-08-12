package pl.klolo.game

import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import pl.klolo.game.event.Event
import pl.klolo.game.event.EventProcessor

enum class Profile {
    DESKTOP,
    ANDROID,
    WEB
}

private val beanDefinition = beans {
    bean<EventProcessor>()
    bean { Stage(ref()) }
    bean { GameEngine(ref(), ref(), ref()) }

    profile("DESKTOP") {
        bean {
            KeyboardProcessor(ref())
        }
    }
}

fun createGameEngine(profile: Profile): GameEngine {
    val applicationContext = GenericApplicationContext()
            .apply {
                environment.setActiveProfiles(profile.name)
                refresh()
            }

    beanDefinition.initialize(applicationContext)
    return applicationContext.getBean(GameEngine::class.java)
}