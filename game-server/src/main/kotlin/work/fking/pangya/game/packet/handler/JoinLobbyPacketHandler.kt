package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.LobbyReplies
import work.fking.pangya.game.packet.outbound.RoomReplies
import work.fking.pangya.game.player.Player

class JoinLobbyPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        player.writeAndFlush(LobbyReplies.ackJoin())

        val serverChannel = player.currentChannel ?: throw IllegalStateException("Player ${player.nickname} attempted to join the lobby but it is not in a server channel")
        val roomManager = serverChannel.roomManager

        player.writeAndFlush(RoomReplies.list(roomManager.activeRooms()))
    }
}
