package pl.klolo.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import pl.klolo.game.engine.gameDependencyInjectionContext
import pl.klolo.game.logic.EntityLogic
import pl.klolo.game.logic.EntityLogicWithRendering
import kotlin.reflect.KType
import kotlin.reflect.full.createType

private var entityCounter = 0

fun <T> emptyFun(): T.() -> Unit = {}

@Suppress("UNCHECKED_CAST")
fun <T : Entity> createEntity(configuration: EntityConfiguration): T {
    return createEntity(configuration, false, emptyFun()) as T
}

@Suppress("UNCHECKED_CAST")
fun <T : Entity> createEntity(configuration: EntityConfiguration, configureEntity: SpriteEntityWithLogic.() -> Unit): T {
    return createEntity(configuration, true, configureEntity) as T
}

@Suppress("UNCHECKED_CAST")
fun createEntity(configuration: EntityConfiguration,
                 forceInitLogic: Boolean,
                 configureEntity: SpriteEntityWithLogic.() -> Unit): Entity {
    return when (configuration.type) {
        EntityType.SPRITE_WITH_LOGIC -> {
            val entityLogic = createLogicClass<SpriteEntityWithLogic>(Class.forName(configuration.logicClass))
            val entitySprite = Sprite(Texture(Gdx.files.internal(configuration.image)))

            SpriteEntityWithLogic(configuration, entityLogic, entitySprite, entityCounter++)
                    .apply(configureEntity)
                    .apply(getInitializeFunction(configuration, forceInitLogic, entityLogic))
        }
        EntityType.ENTITY_WITH_LOGIC -> {
            val entityLogic = createLogicClass<EntityWithLogic>(Class.forName(configuration.logicClass))

            EntityWithLogic(configuration, entityLogic, entityCounter++)
                    .apply(configureEntity as EntityWithLogic.() -> Unit)
                    .apply(getInitializeFunction(configuration, forceInitLogic, entityLogic))
        }
        EntityType.TEXT_ENTITY -> {
            TextEntity(configuration, entityCounter++).apply(configureEntity as TextEntity.() -> Unit)
        }
        EntityType.SPRITE_WITH_CUSTOM_RENDERING -> {
            val entityLogic = createLogicClass<SpriteWithCustomRendering>(Class.forName(configuration.logicClass))

            SpriteWithCustomRendering(configuration, entityLogic as EntityLogicWithRendering<SpriteWithCustomRendering>, entityCounter++)
                    .apply(configureEntity as SpriteWithCustomRendering.() -> Unit)
                    .apply(getInitializeFunction(configuration, forceInitLogic, entityLogic))
        }
    }
}

fun <T : Entity> getInitializeFunction(configuration: EntityConfiguration, forceInitLogic: Boolean, entityLogic: EntityLogic<T>): T.() -> Unit {
    return if (configuration.initOnCreate || forceInitLogic) entityLogic.initialize else emptyFun()
}

@Suppress("UNCHECKED_CAST")
fun <T : Entity> createLogicClass(clazz: Class<*>): EntityLogic<T> {
    val constructParameter = clazz.constructors[0].parameters
            .map { gameDependencyInjectionContext.getBeanByClass(it.type) }
            .toTypedArray()

    return clazz.constructors[0].newInstance(*constructParameter) as EntityLogic<T>
}