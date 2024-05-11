package work.fking.pangya.game.packet.handler.shop

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.ShopReplies
import work.fking.pangya.game.player.Player

class ShopEnterUnknownHandler: ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        player.writeAndFlush(ShopReplies.shopUnknownReply())
    }
}