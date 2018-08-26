package pl.klolo.game.engine

import pl.klolo.game.entity.Entity

fun isPlayerByName(entity: Entity): Boolean {
    return entity.uniqueName.contains("player")
}

fun isShieldByName(entity: Entity): Boolean {
    return entity.uniqueName.contains("shield")
}

fun isEnemyByName(entity: Entity): Boolean {
    return entity.uniqueName.contains("enemy")
}

fun isEnemyLaser(entity: Entity): Boolean {
    return entity.uniqueName.contains("laserRed")
}

fun isPlayerLaser(entity: Entity): Boolean {
    return entity.uniqueName.contains("laserBlue")
}