package work.fking.pangya.game.task

import io.netty.channel.Channel
import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.SessionClient
import work.fking.pangya.game.net.ClientPacketDispatcher
import work.fking.pangya.game.net.ClientPacketType
import work.fking.pangya.game.net.ClientProtocol
import work.fking.pangya.game.net.ProtocolDecoder
import work.fking.pangya.game.packet.outbound.CookieBalancePacket
import work.fking.pangya.game.packet.outbound.EquipmentPacket
import work.fking.pangya.game.packet.outbound.HandoverReplies
import work.fking.pangya.game.packet.outbound.HandoverReplies.HandoverResult
import work.fking.pangya.game.packet.outbound.IffContainerChunkPacket
import work.fking.pangya.game.packet.outbound.MascotRosterPacket
import work.fking.pangya.game.packet.outbound.PangBalancePacket
import work.fking.pangya.game.packet.outbound.ServerChannelsPacket
import work.fking.pangya.game.packet.outbound.TreasureHunterPacket

class HandoverTask(
    private val gameServer:
    GameServer, private val channel: Channel,
    private val cryptKey: Int,
    private val sessionKey: String
) : Runnable {

    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(HandoverTask::class.java)

        @JvmStatic
        private val PROTOCOL = ClientProtocol(ClientPacketType.values())
    }

    override fun run() {
        val sessionInfo: SessionClient.SessionInfo? = try {
            gameServer.sessionClient().loadSession(sessionKey)
        } catch (e: Exception) {
            LOGGER.warn("Handover error sessionKey={}, message={}", sessionKey, e.message)
            channel.writeAndFlush(HandoverReplies.error(HandoverResult.CANNOT_CONNECT_LOGIN_SERVER))
            return
        }
        if (sessionInfo == null) {
            LOGGER.warn("Handover failed, unknown sessionKey={}", sessionKey)
            channel.writeAndFlush(HandoverReplies.error(HandoverResult.CANNOT_CONNECT_LOGIN_SERVER))
            return
        }
        channel.write(HandoverReplies.ok())

        // load all the player stuff
        for (i in 1..15) {
            try {
                // do some fancy fake loading
                channel.writeAndFlush(HandoverReplies.updateProgressBar(i))
                Thread.sleep(25)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
        // if no error, register it
        val player = gameServer.registerPlayer(channel, sessionInfo.uid, sessionInfo.username, sessionInfo.nickname)

        // modify the pipeline
        val pipeline = channel.pipeline()
        pipeline.remove("handoverHandler")
        pipeline.addLast("decoder", ProtocolDecoder(PROTOCOL, cryptKey))
        pipeline.addLast("packetDispatcher", ClientPacketDispatcher(gameServer, player, PROTOCOL.handlers()))

        channel.write(HandoverReplies.handoverReply(player))
        IffContainerChunkPacket.chunk(0x70, player.characterRoster).forEach(channel::write)
        IffContainerChunkPacket.chunk(0x71, player.caddieRoster).forEach(channel::write)
        channel.write(EquipmentPacket(player))
        IffContainerChunkPacket.chunk(0x73, player.inventory).forEach(channel::write)
        channel.write(TreasureHunterPacket())
        channel.write(MascotRosterPacket())
        channel.write(CookieBalancePacket(player))
        channel.write(PangBalancePacket(player))
        channel.writeAndFlush(ServerChannelsPacket(gameServer.serverChannels()))
    }

}
