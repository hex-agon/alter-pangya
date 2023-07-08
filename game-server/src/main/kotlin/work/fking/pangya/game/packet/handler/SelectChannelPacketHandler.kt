package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.SelectChannelResultPacket
import work.fking.pangya.game.player.Player

private val LOGGER = LoggerFactory.getLogger(SelectChannelPacketHandler::class.java)

class SelectChannelPacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val channelId = packet.readUnsignedByte()
        val channel = server.serverChannelById(channelId.toInt()) ?: throw IllegalStateException("unknown serverChannelId=$channelId")
        channel.addPlayer(player)
        LOGGER.info("Player {} joined channel {}", player.nickname, channel.name)
        player.writeAndFlush(SelectChannelResultPacket())
    }

}
