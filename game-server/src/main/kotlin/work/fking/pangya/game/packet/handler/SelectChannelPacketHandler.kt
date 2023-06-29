package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.SelectChannelResultPacket
import work.fking.pangya.game.player.Player

class SelectChannelPacketHandler : ClientPacketHandler {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(SelectChannelPacketHandler::class.java)
    }

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val channelId = packet.readUnsignedByte()
        val channel = server.serverChannelById(channelId.toInt())
        if (channel == null) {
            LOGGER.warn("Player {} tried to join an unknown channel {}", player.nickname(), channelId)
            return
        }
        LOGGER.info("Player {} joined channel {}", player.nickname(), channel.name())
        player.channel().writeAndFlush(SelectChannelResultPacket())
    }

}
