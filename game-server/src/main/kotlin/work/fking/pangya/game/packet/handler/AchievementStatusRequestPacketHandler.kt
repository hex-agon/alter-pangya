package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.AchievementReplies
import work.fking.pangya.game.player.Player

class AchievementStatusRequestPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val uid = packet.readIntLE()
        AchievementReplies.achievementList(player.achievements).forEach(player::write)
        player.writeAndFlush(AchievementReplies.achievementsOk())
    }
}