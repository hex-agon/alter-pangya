package work.fking.pangya.game.packet.handler.clubworkshop

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.ClubSetReplies
import work.fking.pangya.game.player.Player

class ClubWorkshopRankUpPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val cardIffId = packet.readIntLE()
        val cardQuantity = packet.readShortLE()
        val clubSetUid = packet.readIntLE()
        println("cardIffId=$cardIffId, cardQuantity=$cardQuantity, clubSetUid=$clubSetUid")

        val clubSet = player.inventory.findByUid(clubSetUid) ?: throw IllegalStateException("Player ${player.nickname} tried to rank up a clubset it does not own ($clubSetUid)")

        val clubSetStats = clubSet.clubWorkshop.stats
        val statBefore = clubSetStats[0]
        clubSetStats[0]++
        val statAfter = clubSetStats[0]
        player.write(ClubSetReplies.syncClubSetItem(clubSet, statBefore, statAfter))
        player.writeAndFlush(ClubSetReplies.workshopRankUpAck(clubSetUid, 0))
    }
}