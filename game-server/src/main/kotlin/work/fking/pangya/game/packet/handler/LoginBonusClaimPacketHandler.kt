package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.LoginBonusClaimReplyPacket
import work.fking.pangya.game.player.Player

class LoginBonusClaimPacketHandler : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        player.writeAndFlush(LoginBonusClaimReplyPacket())
    }
}
