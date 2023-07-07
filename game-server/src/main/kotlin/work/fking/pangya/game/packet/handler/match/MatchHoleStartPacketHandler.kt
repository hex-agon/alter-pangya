package work.fking.pangya.game.packet.handler.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.model.Coord2D
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.match.PlayerHoleStartEvent

class MatchHoleStartPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val room = player.currentRoom ?: throw IllegalStateException("Player ${player.nickname} started a hole but is not in a room")
        val roomPlayer = room.findSelf(player)

        val hole = packet.readUnsignedByte()
        val unknown1 = packet.readIntLE()
        val unknown2 = packet.readIntLE()
        val par = packet.readByte()
        val teeCoord = Coord2D(packet.readFloatLE(), packet.readFloatLE())
        val holeCoord = Coord2D(packet.readFloatLE(), packet.readFloatLE())


        room.handleMatchEvent(PlayerHoleStartEvent(
            player = roomPlayer,
            hole = hole.toInt(),
            par = par.toInt(),
            teeCoord = teeCoord,
            holeCoord = holeCoord
        ))
    }
}

