package work.fking.pangya.game.packet.handler.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.match.PlayerShotSyncEvent

class MatchPlayerShotSyncPacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val room = player.currentRoom ?: throw IllegalStateException("Player ${player.nickname} tried to sync shot but is not in a room")
        val roomPlayer = room.findSelf(player)

        val connectionId = packet.readIntLE()
        val x = packet.readFloatLE()
        val y = packet.readFloatLE()
        val z = packet.readFloatLE()
        val state = packet.readByte().toInt()
        val bunker = packet.readByte().toInt()
        val unknown = packet.readByte().toInt()
        val pang = packet.readIntLE()
        val bonusPang = packet.readIntLE()
        val cameraFlags = packet.readIntLE()
        val shotFlags = packet.readIntLE()
        val shotFrames = packet.readShortLE().toInt() // i think this is the travel time in frames?
        val grandPrixPenalties = packet.readByte().toInt()

        room.handleMatchEvent(
            PlayerShotSyncEvent(
                player = roomPlayer,
                x = x,
                z = z,
                shotFlags = shotFlags,
                frames = shotFrames
            )
        )
    }
}