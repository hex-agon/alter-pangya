package work.fking.pangya.game.packet.handler.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.RoomReplies
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.Course
import work.fking.pangya.game.room.HoleMode
import work.fking.pangya.game.room.RoomType
import work.fking.pangya.networking.protocol.readPString

class CreateRoomPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val unknown = packet.readByte()
        val shotTime = packet.readIntLE()
        val gameTime = packet.readIntLE()
        val maxPlayers = packet.readByte()
        val roomTypeId = packet.readByte()
        val roomType = RoomType.forId(roomTypeId)
        val holeCount = packet.readByte()
        val course = Course.forId(packet.readByte())
        val holeModeId = packet.readByte()
        val holeMode = HoleMode.forId(holeModeId)
        val unknown3 = packet.readIntLE()
        val name = packet.readPString()
        val password = packet.readPString()
        val artifactIffId = packet.readIntLE()

        val serverChannel = player.currentChannel ?: throw IllegalStateException("Attempted to create a room while not in a server channel")

        val room = serverChannel.roomManager.createRoom(
            name = name,
            password = password,
            roomType = roomType,
            course = course,
            holeMode = holeMode,
            holeCount = holeCount.toInt(),
            maxPlayers = maxPlayers.toInt(),
            shotTime = shotTime,
            gameTime = gameTime,
            artifactIffId = artifactIffId,
            naturalWind = false
        )
        room.addPlayer(player)
    }
}
