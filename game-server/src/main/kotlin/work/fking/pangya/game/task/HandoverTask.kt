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
import work.fking.pangya.game.packet.outbound.MascotRosterPacket
import work.fking.pangya.game.packet.outbound.PangBalancePacket
import work.fking.pangya.game.packet.outbound.ServerChannelsPacket
import work.fking.pangya.game.packet.outbound.TreasureHunterPacket
import work.fking.pangya.game.packet.outbound.chunkIffContainer

private val LOGGER = LoggerFactory.getLogger(HandoverTask::class.java)
private val PROTOCOL = ClientProtocol(ClientPacketType.entries.toTypedArray())

class HandoverTask(
    private val gameServer: GameServer,
    private val channel: Channel,
    private val cryptKey: Int,
    private val sessionKey: String
) : Runnable {

    override fun run() {
        val sessionInfo: SessionClient.SessionInfo? = try {
            gameServer.sessionClient.loadSession(sessionKey)
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

        val persistenceCtx = gameServer.persistenceContext
        val playerId = sessionInfo.uid

        val playerWalletFuture = gameServer.submitTask { persistenceCtx.playerRepository.loadWallet(playerId) }
        val characterRosterFuture = gameServer.submitTask { persistenceCtx.characterRepository.loadRoster(playerId) }
        val caddieRosterFuture = gameServer.submitTask { persistenceCtx.caddieRepository.loadRoster(playerId) }
        val inventoryFuture = gameServer.submitTask { persistenceCtx.inventoryRepository.load(playerId) }
        val equipmentFuture = gameServer.submitTask { persistenceCtx.equipmentRepository.load(playerId) }
        val statisticsFuture = gameServer.submitTask { persistenceCtx.statisticsRepository.load(playerId) }
        val achievementsFuture = gameServer.submitTask { persistenceCtx.achievementsRepository.load(playerId) }

        val futures = listOf(
            playerWalletFuture,
            characterRosterFuture,
            caddieRosterFuture,
            inventoryFuture,
            equipmentFuture,
            statisticsFuture,
            achievementsFuture
        )

        // wait and check if all futures completed normally
        var progress = 1
        for (future in futures) {
            try {
                future.get()
            } catch (e: Exception) {
                LOGGER.warn("Failed to process playerId $playerId handover", e)
                channel.disconnect()
                futures.forEach { it.cancel(true) }
                return
            }
            channel.writeAndFlush(HandoverReplies.updateProgressBar(progress))
            progress++
        }

        val player = gameServer.registerPlayer(
            channel = channel,
            sessionInfo = sessionInfo,
            playerWallet = playerWalletFuture.get(),
            characterRoster = characterRosterFuture.get(),
            caddieRoster = caddieRosterFuture.get(),
            inventory = inventoryFuture.get(),
            equipment = equipmentFuture.get(),
            statistics = statisticsFuture.get(),
            achievements = achievementsFuture.get()
        )

        // modify the pipeline
        val pipeline = channel.pipeline()
        pipeline.remove("handoverHandler")
        pipeline.addLast("decoder", ProtocolDecoder(PROTOCOL, cryptKey))
        pipeline.addLast("packetDispatcher", ClientPacketDispatcher(gameServer, player, PROTOCOL.handlers()))

        channel.write(HandoverReplies.handoverReply(player))
        chunkIffContainer(0x70, player.characterRoster).forEach(channel::write)
        chunkIffContainer(0x71, player.caddieRoster).forEach(channel::write)
        channel.write(EquipmentPacket(player))
        chunkIffContainer(0x73, player.inventory).forEach(channel::write)
        channel.write(TreasureHunterPacket())
        channel.write(MascotRosterPacket())
        channel.write(CookieBalancePacket(player))
        channel.write(PangBalancePacket(player))
        channel.writeAndFlush(ServerChannelsPacket(gameServer.serverChannels))
    }

}
