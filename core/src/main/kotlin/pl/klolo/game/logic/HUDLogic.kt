package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.engine.Highscore
import pl.klolo.game.engine.applicationContext
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.EntityWithLogic
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.AddPoints
import pl.klolo.game.event.EnableSuperBullet
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity

class HUDLogic(
        private val highscore: Highscore,
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<EntityWithLogic> {
    private val textConfiguration = entityRegistry.getConfigurationById("text")
    private val pointsLabel: TextEntity by lazy { initPointLabel() }
    private val bonusLabel: TextEntity by lazy { initLifeLabel() }
    private var points = 0

    private fun initPointLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration, applicationContext)
                .apply {
                    text = "0"
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
    }

    private fun initLifeLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration, applicationContext)
                .apply {
                    text = ""
                    eventProcessor.sendEvent(RegisterEntity(this))
                }
    }

    override val onDispose: EntityWithLogic.() -> Unit = {
        pointsLabel.dispose()
        bonusLabel.dispose()
    }

    override val initialize: EntityWithLogic.() -> Unit = {
        println("HUDLogic creating...")

        eventProcessor
                .subscribe(id)
                .onEvent(AddPoints::class.java) {
                    points += it.points
                    pointsLabel.text = "$points"
                }
                .onEvent(EnableSuperBullet) {
                    bonusLabel.text = "" // TODO
                }
    }

    override val onUpdate: EntityWithLogic.(Float) -> Unit = {
        val leftMargin = 10f
        pointsLabel.setPosition(leftMargin, Gdx.graphics.height.toFloat() - pointsLabel.getFontHeight())
        bonusLabel.setPosition(
                Gdx.graphics.width.toFloat() - 100,
                Gdx.graphics.height.toFloat() - pointsLabel.getFontHeight() * 3)
    }
}