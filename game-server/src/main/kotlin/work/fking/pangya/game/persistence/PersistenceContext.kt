package work.fking.pangya.game.persistence

data class PersistenceContext(
    val playerRepository: PlayerRepository = DefaultPlayerRepository(),
    val characterRepository: CharacterRepository = DefaultCharacterRepository(),
    val caddieRepository: CaddieRepository = DefaultCaddieRepository(),
    val inventoryRepository: InventoryRepository = DefaultInventoryRepository(),
    val equipmentRepository: EquipmentRepository = DefaultEquipmentRepository(),
    val statisticsRepository: StatisticsRepository = DefaultStatisticsRepository(),
    val achievementsRepository: AchievementsRepository = DefaultAchievementsRepository()
)