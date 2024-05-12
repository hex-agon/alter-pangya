package work.fking.pangya.game.packet.handler.myroom

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player

class MyRoomOpenPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val selfPlayerUid = packet.readIntLE()
        val targetPlayerUid = packet.readIntLE()
    }
}
