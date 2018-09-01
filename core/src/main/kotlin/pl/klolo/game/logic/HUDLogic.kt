package pl.klolo.game.logic

import com.badlogic.gdx.Gdx
import pl.klolo.game.entity.EntityRegistry
import pl.klolo.game.entity.EntityWithLogic
import pl.klolo.game.entity.TextEntity
import pl.klolo.game.entity.createEntity
import pl.klolo.game.event.AddPoints
import pl.klolo.game.event.EnableDoublePoints
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.RegisterEntity
import pl.klolo.game.extensions.executeAfterDelay
import pl.klolo.game.logic.player.bonusLifetime

class HUDLogic(
        private val eventProcessor: EventProcessor,
        private val entityRegistry: EntityRegistry) : EntityLogic<EntityWithLogic> {
    private val textConfiguration = entityRegistry.getConfigurationById("text")
    private val pointsLabel: TextEntity by lazy { initPointLabel() }
    private val bonusLabel: TextEntity by lazy { initBonusLabel() }
    private var points = 0
    var doublePoints = false

    private fun initPointLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration)
                .apply {
                    text = "0"
                    eventProcessor.sendEvent(RegisterEntity(this))
                    intializeFont()
                }
    }

    private fun initBonusLabel(): TextEntity {
        return createEntity<TextEntity>(textConfiguration)
                .apply {
                    text = ""
                    eventProcessor.sendEvent(RegisterEntity(this))
                    intializeFont()
                }
    }

    override val onDispose: EntityWithLogic.() -> Unit = {
        pointsLabel.dispose()
    }

    override val initialize: EntityWithLogic.() -> Unit = {
        Gdx.app.debug(this.javaClass.name,"createSubscription")

        eventProcessor
                .subscribe(id)
                .onEvent(AddPoints::class.java) {
                    addPoints(it)
                    pointsLabel.text = "$points"
                }
                .onEvent(EnableDoublePoints) {
                    doublePoints = true
                    bonusLabel.text = "x2"
                    executeAfterDelay(bonusLifetime) {
                        doublePoints = false
                        bonusLabel.text = ""
                    }
                }
    }

    override val onUpdate: EntityWithLogic.(Float) -> Unit = {
        val leftMargin = 10f
        pointsLabel.setPosition(leftMargin, Gdx.graphics.height.toFloat() - pointsLabel.getFontHeight() * 1.2f)
        bonusLabel.setPosition(
                Gdx.graphics.width.toFloat() - bonusLabel.getFontWidth() * 2f,
                Gdx.graphics.height.toFloat() - pointsLabel.getFontHeight() * 3f)
    }

    private fun addPoints(it: AddPoints) {
        points = when (doublePoints) {
            true -> points + (it.points * 2)
            false -> points + it.points
        }
    }
}