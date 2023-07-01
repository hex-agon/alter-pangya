package work.fking.pangya.game.packet.handler.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.RoomReplies
import work.fking.pangya.game.player.Player

class LeaveRoomPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val room = player.currentRoom ?: throw IllegalStateException("Player is not in a room")

        val unknown1 = packet.readByte() // unknown (either 0 or 1)
        val targetRoomId = packet.readShortLE()
        val unknown2 = packet.readIntLE() // unknown
        val unknown3 = packet.readIntLE() // unknown
        val unknown4 = packet.readIntLE() // unknown
        val unknown5 = packet.readIntLE() // unknown

        room.removePlayer(player)
        player.writeAndFlush(RoomReplies.leaveAck())
    }
}
