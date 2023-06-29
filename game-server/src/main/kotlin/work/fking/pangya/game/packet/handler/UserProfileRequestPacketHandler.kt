package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.UserStatisticsReplies
import work.fking.pangya.game.player.Player

class UserProfileRequestPacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val userId = packet.readIntLE()
        val type = packet.readByte()
        val channel = player.channel()

        if (type.toInt() == 5) {
            channel.write(UserStatisticsReplies.username(type.toInt(), userId))
            channel.write(UserStatisticsReplies.character(player))
            channel.write(UserStatisticsReplies.equipment(type.toInt(), player))
        }
        channel.write(UserStatisticsReplies.userStatistic(type.toInt(), userId))
        channel.write(UserStatisticsReplies.courseStatistic(type.toInt(), userId))
        channel.write(UserStatisticsReplies.trophies(type.toInt(), userId))
        channel.write(UserStatisticsReplies.ack(true, type.toInt(), userId))
        channel.flush()
    }
}
