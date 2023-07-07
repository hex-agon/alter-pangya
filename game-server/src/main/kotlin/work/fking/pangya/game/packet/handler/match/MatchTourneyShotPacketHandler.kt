package work.fking.pangya.game.packet.handler.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.match.PlayerTourneyShotEvent
import work.fking.pangya.game.room.match.TourneyShotData

class MatchTourneyShotPacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val room = player.currentRoom ?: throw IllegalStateException("Player ${player.nickname} tried to sync tourney shot but is not in a room")
        val roomPlayer = room.findSelf(player)

        val shotData = TourneyShotData(packet)

        room.handleMatchEvent(PlayerTourneyShotEvent(roomPlayer, shotData))
    }
}

