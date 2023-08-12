package work.fking.pangya.game.packet.handler.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.courseById
import work.fking.pangya.game.room.holeModeById
import work.fking.pangya.game.room.roomTypeById
import work.fking.pangya.networking.protocol.readPString
import java.time.Duration
import java.time.temporal.ChronoUnit

class CreateRoomPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val unknown = packet.readByte()
        val shotTime = packet.readIntLE()
        val gameTime = packet.readIntLE()
        val maxPlayers = packet.readByte()
        val roomTypeId = packet.readByte()
        val roomType = roomTypeById(roomTypeId)
        val holeCount = packet.readByte()
        val course = courseById(packet.readByte())
        val holeModeId = packet.readByte()
        val holeMode = holeModeById(holeModeId)
        val unknown3 = packet.readIntLE()
        val name = packet.readPString()
        val password = packet.readPString()
        val artifactIffId = packet.readIntLE()

        val serverChannel = player.currentChannel ?: throw IllegalStateException("Player ${player.nickname} attempted to create a room while not in a server channel")

        val room = serverChannel.roomManager.createRoom(
            name = name,
            password = password,
            roomType = roomType,
            course = course,
            holeMode = holeMode,
            holeCount = holeCount.toInt(),
            maxPlayers = maxPlayers.toInt(),
            shotTime = Duration.of(shotTime.toLong(), ChronoUnit.MILLIS),
            gameTime = Duration.of(gameTime.toLong(), ChronoUnit.MILLIS),
            artifactIffId = artifactIffId,
            naturalWind = false
        )
        room.attemptJoin(player)
    }
}
