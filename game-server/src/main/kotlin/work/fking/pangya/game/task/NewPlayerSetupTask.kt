package work.fking.pangya.game.task

import org.slf4j.LoggerFactory
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Character
import work.fking.pangya.game.player.Equipment
import work.fking.pangya.game.player.Item
import work.fking.pangya.game.player.PlayerStatistics
import work.fking.pangya.game.player.characterBaseParts
import java.util.concurrent.Callable

private val LOGGER = LoggerFactory.getLogger(NewPlayerSetupTask::class.java)

private const val airKnightClubSetIffId = 0x10000000
private const val baseCometIffId = 0x14000000

class NewPlayerSetupTask(
    private val playerUid: Int,
    private val characterIffId: Int,
    private val characterHairColor: Int,
    private val persistenceContext: PersistenceContext
) : Callable<Unit> {

    override fun call() {
        // TODO: We have to run everything within a transaction, somehow
        LOGGER.debug("Creating base entities for playerUid=$playerUid")
        persistenceContext.statisticsRepository.save(playerUid, PlayerStatistics())

        val character = persistenceContext.characterRepository.save(
            playerUid, Character(
                iffId = characterIffId,
                hairColor = characterHairColor,
                partIffIds = characterBaseParts(characterIffId)
            )
        )

        val clubSet = persistenceContext.inventoryRepository.saveItem(
            playerUid, Item(iffId = airKnightClubSetIffId)
        )
        persistenceContext.inventoryRepository.saveItem(
            playerUid, Item(iffId = baseCometIffId)
        )

        persistenceContext.equipmentRepository.save(
            playerUid, Equipment(
                characterUid = character.uid,
                clubSetUid = clubSet.uid,
                cometIffId = baseCometIffId
            )
        )

        persistenceContext.achievementsRepository.createAchievements(playerUid)
    }
}