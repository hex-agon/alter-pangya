package work.fking.pangya.game.persistence

import work.fking.pangya.game.player.Equipment

interface EquipmentRepository {
    fun load(playerId: Int): Equipment
}

class DefaultEquipmentRepository : EquipmentRepository {
    override fun load(playerId: Int): Equipment = Equipment(
        equippedClubSetUid = 200,
        equippedCharacterUid = 100,
        equippedCometIffId = 0x14000000
    )
}