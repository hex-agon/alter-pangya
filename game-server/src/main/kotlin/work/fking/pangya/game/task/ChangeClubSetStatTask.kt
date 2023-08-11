package work.fking.pangya.game.task

import work.fking.pangya.game.model.IFF_TYPE_CLUBSET
import work.fking.pangya.game.model.iffTypeFromId
import work.fking.pangya.game.packet.outbound.ClubSetReplies
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Item
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.Stat
import work.fking.pangya.game.player.Stat.ACCURACY
import work.fking.pangya.game.player.Stat.CONTROL
import work.fking.pangya.game.player.Stat.CURVE
import work.fking.pangya.game.player.Stat.POWER
import work.fking.pangya.game.player.Stat.SPIN

private const val TYPE_UPGRADE_CLUBSET = 1
private const val TYPE_DOWNGRADE_CLUBSET = 3

class ChangeClubSetStatTask(
    private val persistenceCtx: PersistenceContext,
    private val player: Player,
    private val itemUid: Int,
    private val type: Int,
    private val stat: Stat
) : Runnable {


    override fun run() {
        val clubSet = player.inventory.findByUid(itemUid) ?: throw IllegalStateException("Player ${player.nickname} tried to upgrade an item it does not own")
        require(iffTypeFromId(clubSet.iffId) == IFF_TYPE_CLUBSET) { "Player ${player.nickname} tried to upgrade an item that is not a ClubSet" }

        when (type) {
            TYPE_UPGRADE_CLUBSET -> upgrade(clubSet)
            TYPE_DOWNGRADE_CLUBSET -> downgrade(clubSet)
            else -> throw IllegalStateException("Player ${player.nickname} tried to upgrade an item but the type is invalid ($type)")
        }
    }

    private fun upgrade(clubSet: Item) {
        val level = clubSet.stats[stat.ordinal]
        val cost = (level + 1) * when (stat) {
            POWER -> 2100
            CONTROL -> 1700
            ACCURACY -> 2400
            SPIN, CURVE -> 1900
        }
        val wallet = player.wallet
        require(wallet.pangBalance >= cost) { "Player ${player.nickname} tried to upgrade an item but doesn't have enough pang" }
        // TODO: verify clubset upgrade limits

        persistenceCtx.transactional { tx ->
            wallet.pangBalance -= cost
            clubSet.stats[stat.ordinal]++
            persistenceCtx.playerRepository.saveWallet(tx, player.uid, wallet)
            persistenceCtx.inventoryRepository.saveItem(tx, player.uid, clubSet)
        }
        player.writeAndFlush(ClubSetReplies.upgradeAck(type, stat.ordinal, itemUid, cost.toLong()))
    }

    private fun downgrade(clubSet: Item) {
        require(clubSet.stats[stat.ordinal] > 0) { "Player ${player.nickname} tried to downgrade an item but the stat is already at 0" }

        clubSet.stats[stat.ordinal]--
        persistenceCtx.inventoryRepository.saveItem(persistenceCtx.noTxContext(), player.uid, clubSet)
        player.writeAndFlush(ClubSetReplies.upgradeAck(type, stat.ordinal, itemUid, 0))
    }
}

