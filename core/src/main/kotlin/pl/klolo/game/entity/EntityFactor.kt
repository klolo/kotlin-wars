package pl.klolo.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import org.springframework.context.ApplicationContext
import pl.klolo.game.logic.EntityLogic

enum class EntityType {
    SPRITE_WITH_LOGIC
}

class EntityConfiguration(
        val uniqueId: String,
        val logicClass: String,
        val type: EntityType,
        val image: String,
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        var zIndex: Int
)

fun createEntity(configuration: EntityConfiguration, applicationContext: ApplicationContext): Entity {
    return createEntity(configuration, applicationContext) {

    }
}

fun createEntity(configuration: EntityConfiguration, applicationContext: ApplicationContext, configureEntity: SpriteEntityWithLogic.() -> Unit): Entity {

    return when (configuration.type) {
        EntityType.SPRITE_WITH_LOGIC -> {
            val entityLogic = createLogicClass<SpriteEntityWithLogic>(Class.forName(configuration.logicClass), applicationContext)
            val sprite = Sprite(Texture(Gdx.files.internal(configuration.image)))
            SpriteEntityWithLogic(configuration.uniqueId, entityLogic, sprite, configuration.zIndex)
                    .apply {
                        x = configuration.x
                        y = configuration.y
                        width = configuration.width
                        height = configuration.height
                    }
                    .apply(configureEntity)
                    .apply(entityLogic.initialize)
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Entity> createLogicClass(clazz: Class<*>, applicationContext: ApplicationContext): EntityLogic<T> {
    val constructParameter = clazz.constructors[0].parameters
            .map { applicationContext.getBean(it.type) }
            .toTypedArray()

    return clazz.constructors[0].newInstance(*constructParameter) as EntityLogic<T>
}