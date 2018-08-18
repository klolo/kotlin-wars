package pl.klolo.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import org.springframework.context.ApplicationContext
import pl.klolo.game.logic.EntityLogic

private var entityCounter = 0

fun <T> emptyFun(): T.() -> Unit = {}

@Suppress("UNCHECKED_CAST")
fun <T: Entity> createEntity(configuration: EntityConfiguration, applicationContext: ApplicationContext): T {
    return createEntity(configuration, applicationContext, emptyFun(), false) as T
}

@Suppress("UNCHECKED_CAST")
fun <T: Entity> createEntity(configuration: EntityConfiguration,
                 applicationContext: ApplicationContext,
                 configureEntity: SpriteEntityWithLogic.() -> Unit): T {
    return createEntity(configuration, applicationContext, configureEntity, true) as T
}

@Suppress("UNCHECKED_CAST")
fun createEntity(configuration: EntityConfiguration,
                 applicationContext: ApplicationContext,
                 configureEntity: SpriteEntityWithLogic.() -> Unit,
                 forceInitLogic: Boolean): Entity {
    return when (configuration.type) {
        EntityType.SPRITE_WITH_LOGIC -> {
            val entityLogic = createLogicClass<SpriteEntityWithLogic>(Class.forName(configuration.logicClass), applicationContext)
            val entitySprite = Sprite(Texture(Gdx.files.internal(configuration.image)))

            SpriteEntityWithLogic(configuration, entityLogic, entitySprite, entityCounter++)
                    .apply(configureEntity)
                    .apply(getInitializeFunction(configuration, forceInitLogic, entityLogic))
        }
        EntityType.ENTITY_WITH_LOGIC -> {
            val entityLogic = createLogicClass<EntityWithLogic>(Class.forName(configuration.logicClass), applicationContext)

            EntityWithLogic(configuration, entityLogic, entityCounter++)
                    .apply(configureEntity as EntityWithLogic.() -> Unit)
                    .apply(getInitializeFunction(configuration, forceInitLogic, entityLogic))
        }
        EntityType.TEXT_ENTITY -> {
            TextEntity(configuration, entityCounter++).apply(configureEntity as TextEntity.() -> Unit)
        }
    }
}

fun <T : Entity> getInitializeFunction(configuration: EntityConfiguration, forceInitLogic: Boolean, entityLogic: EntityLogic<T>): T.() -> Unit {
    return if (configuration.initOnCreate || forceInitLogic) entityLogic.initialize else emptyFun()
}

@Suppress("UNCHECKED_CAST")
fun <T : Entity> createLogicClass(clazz: Class<*>, applicationContext: ApplicationContext): EntityLogic<T> {
    val constructParameter = clazz.constructors[0].parameters
            .map { applicationContext.getBean(it.type) }
            .toTypedArray()

    return clazz.constructors[0].newInstance(*constructParameter) as EntityLogic<T>
}