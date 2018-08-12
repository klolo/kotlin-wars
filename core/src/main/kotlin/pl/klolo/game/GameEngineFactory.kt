package pl.klolo.game

import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import pl.klolo.game.event.EventProcessor

private val applicationContext = GenericApplicationContext()
        .apply {
            refresh()
        }

private val beanDefinition = beans {
    bean<EventProcessor>()
    bean { GameEngine(ref()) }

    profile("desktop") {
        bean<KeyboardProcessor>()
    }
}

fun createGameEngine(): GameEngine {
    beanDefinition.initialize(applicationContext)
    return applicationContext.getBean(GameEngine::class.java)
}