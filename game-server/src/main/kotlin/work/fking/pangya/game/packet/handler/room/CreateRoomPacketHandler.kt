package work.fking.pangya.game.packet.handler.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.common.Rand
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.model.Course
import work.fking.pangya.game.model.HoleMode
import work.fking.pangya.game.model.RoomType
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.RoomResponses
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.readPString

class CreateRoomPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val unknown = packet.readByte()
        val shotTime = packet.readIntLE()
        val gameTime = packet.readIntLE()
        val maxPlayers = packet.readByte()
        val roomTypeId = packet.readByte()
        val roomType = RoomType.forId(roomTypeId) ?: throw IllegalStateException("Unsupported roomTypeId=$roomTypeId")
        val holeCount = packet.readByte()
        val course = Course.forId(packet.readByte())
        val holeModeId = packet.readByte()
        val holeMode = HoleMode.forId(holeModeId) ?: throw IllegalStateException("Unsupported holeModeId=$holeModeId")
        val unknown3 = packet.readIntLE()
        val name = packet.readPString()
        val password = packet.readPString()
        val artifactIffId = packet.readIntLE()
        val channel = player.channel()

        channel.write(RoomResponses.createSuccess(name, roomType, Rand.maxInclusive(127)))
        channel.write(RoomResponses.roomInfo(roomType, name, course, holeMode, holeCount.toInt(), maxPlayers.toInt(), shotTime, gameTime))
        channel.write(RoomResponses.roomInitialCensus(player))
        channel.write(RoomResponses.loungePkt196())
        channel.write(RoomResponses.loungePkt9e())
        channel.flush()
    }
}
