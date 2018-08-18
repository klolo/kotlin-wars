package pl.klolo.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.sun.org.apache.xpath.internal.operations.Bool
import org.springframework.context.ApplicationContext
import pl.klolo.game.logic.EntityLogic

private var entityCounter = 0

enum class EntityType {
    SPRITE_WITH_LOGIC
}

class EntityConfiguration(
        val uniqueName: String,
        val logicClass: String,
        val type: EntityType,
        val image: String,
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        var zIndex: Int,
        var initOnCreate: Boolean
)

private val emptyInitFun: SpriteEntityWithLogic.() -> Unit = {}

fun createEntity(configuration: EntityConfiguration, applicationContext: ApplicationContext): Entity {
    return createEntity(configuration, applicationContext, emptyInitFun, false)
}

fun createEntity(configuration: EntityConfiguration,
                 applicationContext: ApplicationContext,
                 configureEntity: SpriteEntityWithLogic.() -> Unit): Entity {
    return createEntity(configuration, applicationContext, configureEntity, true)
}

fun createEntity(configuration: EntityConfiguration,
                 applicationContext: ApplicationContext,
                 configureEntity: SpriteEntityWithLogic.() -> Unit,
                 forceInitLogic: Boolean): Entity {

    return when (configuration.type) {
        EntityType.SPRITE_WITH_LOGIC -> {
            val entityLogic = createLogicClass<SpriteEntityWithLogic>(Class.forName(configuration.logicClass), applicationContext)
            val sprite = Sprite(Texture(Gdx.files.internal(configuration.image)))

            val logicInitializeFunction: SpriteEntityWithLogic.() -> Unit =
                    if (configuration.initOnCreate || forceInitLogic) entityLogic.initialize else emptyInitFun

            SpriteEntityWithLogic(configuration.uniqueName, entityLogic, sprite, configuration.zIndex, entityCounter++)
                    .apply {
                        x = configuration.x
                        y = configuration.y
                        width = configuration.width
                        height = configuration.height
                    }
                    .apply(configureEntity)
                    .apply(logicInitializeFunction)
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