package work.fking.pangya.game.packet.handler.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.match.PlayerShotCommitEvent
import work.fking.pangya.game.room.match.ShotCommitData

class MatchPlayerShotCommitPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val room = player.currentRoom ?: throw IllegalStateException("Player ${player.nickname} finished a hole but is not in a room")
        val roomPlayer = room.findSelf(player)

        val shotType = packet.readShortLE().toInt()

        if (shotType == 1) { // ??
            val bytes = ByteArray(9)
            packet.readBytes(bytes)
        }
        val shotData = ShotCommitData(
            maxClick = packet.readFloatLE(),
            minClick = packet.readFloatLE(),
            curve = packet.readFloatLE(),
            spin = packet.readFloatLE(),
            accuracy = packet.readUnsignedByte().toInt(),
            special = packet.readIntLE(),
            frame = packet.readIntLE(),
            shotAngle = packet.readFloatLE(),
            shotTime = packet.readIntLE(),
            center = packet.readFloatLE(),
            clubIndex = packet.readUnsignedByte().toInt(),
            randomPower = packet.readFloatLE(),
            randomAngle = packet.readFloatLE(),
            impactWidth = packet.readFloatLE(),
            windPower = packet.readFloatLE(),
            windDirection = packet.readFloatLE()
        )
        room.handleMatchEvent(PlayerShotCommitEvent(roomPlayer, shotData))
    }
}

