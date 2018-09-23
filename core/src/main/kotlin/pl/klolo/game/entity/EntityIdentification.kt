package pl.klolo.game.entity

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

fun isExtraPointsBonus(entity: Entity): Boolean {
    return entity.uniqueName == "starBonus"
}

fun isExtraBonus(entity: Entity): Boolean {
    return entity.uniqueName.contains("Bonus")
}

