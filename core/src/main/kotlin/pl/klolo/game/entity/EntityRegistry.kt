package pl.klolo.game.entity

class EntityRegistry {
    private var entitiesConfiguration: List<EntityConfiguration> = mutableListOf()

    fun addConfiguration(entitiesConfiguration: List<EntityConfiguration>) {
        this.entitiesConfiguration += entitiesConfiguration
    }

    fun getConfigurationById(id: String): EntityConfiguration {
        return entitiesConfiguration.findLast {
            it.uniqueName == id
        } ?: throw IllegalArgumentException("Entity configuration by id not found $id")
    }
}