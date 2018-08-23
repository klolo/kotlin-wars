package pl.klolo.game.logic

import box2dLight.PointLight
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.scenes.scene2d.Action
import pl.klolo.game.entity.EntityConfiguration
import pl.klolo.game.extensions.execute

class PlayerState {
    var lifeLevel = 100
    var points = 0
    val defaulBulletPower = 10
    var enabledSuperBulletCounter = 0
    var bulletPower = defaulBulletPower
    var currentMove: Action = execute(Runnable {})

    lateinit var physicsShape: PolygonShape
    lateinit var body: Body
    lateinit var playerLight: PointLight
    lateinit var laserConfiguration: EntityConfiguration
}