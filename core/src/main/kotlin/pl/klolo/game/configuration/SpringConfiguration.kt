package pl.klolo.game.configuration

import org.springframework.context.support.beans
import pl.klolo.game.*
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.event.EventProcessor

enum class Profile {
    DESKTOP,
    ANDROID,
    WEB
}

val beanDefinition = beans {
    bean<EventProcessor>()
    bean<GamePhysics>()
    bean<EntityRegistry>()

    bean { Stage(ref(), ref()) }
    bean { GameLighting(ref()) }
    bean { GameEngine(ref(), ref(), ref(), ref()) }

    profile("DESKTOP") {
        bean {
            KeyboardProcessor(ref())
        }
    }
}