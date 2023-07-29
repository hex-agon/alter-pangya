package work.fking.pangya.game.persistence

data class PersistenceContext(
    val playerRepository: PlayerRepository = InMemoryPlayerRepository(),
    val characterRepository: CharacterRepository = InMemoryCharacterRepository(),
    val caddieRepository: CaddieRepository = InMemoryCaddieRepository(),
    val inventoryRepository: InventoryRepository = InMemoryInventoryRepository(),
    val equipmentRepository: EquipmentRepository = InMemoryEquipmentRepository(),
    val statisticsRepository: StatisticsRepository = InMemoryStatisticsRepository(),
    val achievementsRepository: AchievementsRepository = InMemoryAchievementsRepository()
)