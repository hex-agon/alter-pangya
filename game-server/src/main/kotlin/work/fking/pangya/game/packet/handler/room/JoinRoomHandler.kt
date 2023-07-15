package work.fking.pangya.game.packet.handler.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.RoomJoinResponses
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.RoomJoinError.ROOM_DOES_NOT_EXIST

class JoinRoomHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val serverChannel = player.currentChannel ?: throw IllegalStateException("Player ${player.nickname} attempted to create a room while not in a server channel")
        val roomNumber = packet.readIntLE()

        val room = serverChannel.roomManager.findRoom(roomNumber)

        if (room == null) {
            player.writeAndFlush(RoomJoinResponses.error(ROOM_DOES_NOT_EXIST))
            return
        }
        room.attemptJoin(player)?.let { error -> player.writeAndFlush(RoomJoinResponses.error(error)) }
    }
}