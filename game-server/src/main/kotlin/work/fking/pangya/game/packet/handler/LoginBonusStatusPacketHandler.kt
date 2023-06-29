package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.LoginBonusReplyPacket
import work.fking.pangya.game.player.Player

class LoginBonusStatusPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        player.channel().writeAndFlush(LoginBonusReplyPacket())
    }
}
