package work.fking.pangya.game.packet.handler.clubworkshop

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.statById
import work.fking.pangya.game.task.ChangeClubSetStatTask

class ClubSetUpgradePacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val type = packet.readByte() // clubset upgrade = 1, clubset downgrade = 3
        val statId = packet.readUnsignedByte()
        val itemUid = packet.readIntLE()

        val stat = statById(statId.toInt()) ?: throw IllegalStateException("Player ${player.nickname} tried to upgrade an item with an invalid stat ($statId)")

        server.runTask(
            ChangeClubSetStatTask(
                persistenceCtx = server.persistenceCtx,
                player = player,
                itemUid = itemUid,
                type = type.toInt(),
                stat = stat
            )
        )
    }
}