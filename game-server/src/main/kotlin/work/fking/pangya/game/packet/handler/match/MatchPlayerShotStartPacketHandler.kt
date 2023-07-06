package work.fking.pangya.game.packet.handler.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.match.PlayerShotStartEvent

class MatchPlayerShotStartPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val room = player.currentRoom ?: throw IllegalStateException("Player ${player.nickname} finished a hole but is not in a room")
        val roomPlayer = room.findSelf(player)
        // 0 = idle, 1 = going up, 2 = going down
        val state = packet.readUnsignedByte()
        val click = packet.readFloatLE()
        room.handleMatchEvent(PlayerShotStartEvent(roomPlayer))
    }
}