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
    private val persistenceCtx: PersistenceContext
) : Callable<Unit> {

    override fun call() {
        persistenceCtx.transactional { tx ->
            LOGGER.debug("Creating base entities for playerUid=$playerUid")
            persistenceCtx.statisticsRepository.save(tx, playerUid, PlayerStatistics())

            val character = persistenceCtx.characterRepository.save(
                txCtx = tx,
                playerUid = playerUid,
                character = Character(
                    iffId = characterIffId,
                    hairColor = characterHairColor,
                    partIffIds = characterBaseParts(characterIffId)
                )
            )

            val clubSet = persistenceCtx.inventoryRepository.saveItem(
                txCtx = tx,
                playerUid = playerUid,
                item = Item(iffId = airKnightClubSetIffId)
            )
            persistenceCtx.inventoryRepository.saveItem(
                txCtx = tx,
                playerUid = playerUid,
                item = Item(iffId = baseCometIffId)
            )

            persistenceCtx.equipmentRepository.save(
                txCtx = tx,
                playerUid = playerUid,
                equipment = Equipment(
                    characterUid = character.uid,
                    clubSetUid = clubSet.uid,
                    cometIffId = baseCometIffId
                )
            )

            persistenceCtx.achievementsRepository.createAchievements(tx, playerUid)
        }
    }
}
