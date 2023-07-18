package work.fking.pangya.game.packet.handler.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.MatchReplies
import work.fking.pangya.game.player.Player

class MatchPlayerUseTimeBoosterPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val room = player.currentRoom ?: throw IllegalStateException("Player ${player.nickname} tried to use a time booster but is not in a room")
        val roomPlayer = room.findSelf(player)
        val boostValue = packet.readFloatLE()

        roomPlayer.writeAndFlush(MatchReplies.useTimeBoosterAck(roomPlayer, boostValue))
    }
}