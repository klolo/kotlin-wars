package pl.klolo.game.configuration

import org.springframework.context.support.beans
import pl.klolo.game.engine.*
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.physics.ContactListener
import pl.klolo.game.physics.GamePhysics

enum class Profile {
    DESKTOP,
    ANDROID,
    WEB
}

val beanDefinition = beans {
    bean<EventProcessor>()
    bean<EntityRegistry>()
    bean<Highscore>()

    bean { SoundManager(ref()) }
    bean { ContactListener(ref()) }
    bean { GamePhysics(ref()) }
    bean { Stage(ref(), ref(), ref(), ref()) }
    bean { GameLighting(ref()) }
    bean { GameEngine(ref(), ref(), ref(), ref()) }

    profile("DESKTOP") {
        bean {
            KeyboardProcessor(ref())
        }
    }
}