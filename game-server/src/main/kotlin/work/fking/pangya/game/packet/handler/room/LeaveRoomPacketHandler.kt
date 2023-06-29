package work.fking.pangya.game.packet.handler.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.RoomResponses
import work.fking.pangya.game.player.Player

class LeaveRoomPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        packet.readByte() // unknown (either 0 or 1)
        val roomId = packet.readShortLE()
        packet.readIntLE() // unknown
        packet.readIntLE() // unknown
        packet.readIntLE() // unknown
        packet.readIntLE() // unknown
        player.channel().writeAndFlush(RoomResponses.leaveSuccess())
    }
}
