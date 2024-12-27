package work.fking.pangya.game.task

import io.netty.channel.Channel
import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.session.SessionClient
import work.fking.pangya.game.net.ClientPacketDispatcher
import work.fking.pangya.game.net.ClientPacketType
import work.fking.pangya.game.net.ClientProtocol
import work.fking.pangya.game.net.ProtocolDecoder
import work.fking.pangya.game.packet.outbound.CardholicReplies
import work.fking.pangya.game.packet.outbound.CookieBalancePacket
import work.fking.pangya.game.packet.outbound.EquipmentPacket
import work.fking.pangya.game.packet.outbound.HandoverReplies
import work.fking.pangya.game.packet.outbound.HandoverReplies.HandoverResult
import work.fking.pangya.game.packet.outbound.MascotRosterPacket
import work.fking.pangya.game.packet.outbound.PangBalancePacket
import work.fking.pangya.game.packet.outbound.ServerChannelsPacket
import work.fking.pangya.game.packet.outbound.TreasureHunterPacket
import work.fking.pangya.game.packet.outbound.chunkIffContainer
import work.fking.pangya.game.session.SessionInfo
import java.util.concurrent.TimeUnit

private val LOGGER = LoggerFactory.getLogger(HandoverTask::class.java)
private val PROTOCOL = ClientProtocol(ClientPacketType.entries.toTypedArray())

class HandoverTask(
    private val gameServer: GameServer,
    private val channel: Channel,
    private val cryptKey: Int,
    private val loginKey: String
) : Runnable {

    override fun run() {
        val sessionInfo: SessionInfo? = try {
            gameServer.sessionClient.loadSessionInfo(loginKey)
        } catch (e: Exception) {
            LOGGER.warn("Handover error loginKey={}, message={}", loginKey, e.message)
            channel.writeAndFlush(HandoverReplies.error(HandoverResult.CANNOT_CONNECT_LOGIN_SERVER))
            return
        }
        if (sessionInfo == null) {
            LOGGER.warn("Handover failed, unknown loginKey={}", loginKey)
            channel.writeAndFlush(HandoverReplies.error(HandoverResult.CANNOT_CONNECT_LOGIN_SERVER))
            return
        }
        channel.write(HandoverReplies.ok())

        val playerUid = sessionInfo.uid

        // The player picked a new character on the login server, this is a brand-new account
        sessionInfo.characterIffId?.let {
            val future = gameServer.submitTask(
                NewPlayerSetupTask(
                    playerUid = sessionInfo.uid,
                    characterIffId = sessionInfo.characterIffId,
                    characterHairColor = sessionInfo.characterHairColor ?: 0,
                    persistenceCtx = gameServer.persistenceCtx
                )
            )
            try {
                future.get(10, TimeUnit.SECONDS)
            } catch (e: Exception) {
                LOGGER.warn("Failed to process playerId $playerUid handover", e)
                channel.disconnect()
                return
            }
        }

        val persistenceCtx = gameServer.persistenceCtx

        val updateNicknameFuture = gameServer.submitTask { persistenceCtx.noTx { tx -> playerRepository.updateNickname(tx, playerUid, sessionInfo.nickname) } }
        val playerWalletFuture = gameServer.submitTask { persistenceCtx.noTx { tx -> playerRepository.loadWallet(tx, playerUid) } }
        val characterRosterFuture = gameServer.submitTask { persistenceCtx.noTx { tx -> characterRepository.loadRoster(tx, playerUid) } }
        val caddieRosterFuture = gameServer.submitTask { persistenceCtx.noTx { tx -> caddieRepository.loadRoster(tx, playerUid) } }
        val inventoryFuture = gameServer.submitTask { persistenceCtx.noTx { tx -> inventoryRepository.load(tx, playerUid) } }
        val cardInventoryFuture = gameServer.submitTask { persistenceCtx.noTx { tx -> cardRepository.load(tx, playerUid) } }
        val equipmentFuture = gameServer.submitTask { persistenceCtx.noTx { tx -> equipmentRepository.load(tx, playerUid) } }
        val statisticsFuture = gameServer.submitTask { persistenceCtx.noTx { tx -> statisticsRepository.load(tx, playerUid) } }
        val achievementsFuture = gameServer.submitTask { persistenceCtx.noTx { tx -> achievementsRepository.load(tx, playerUid) } }

        val futures = listOf(
            updateNicknameFuture,
            playerWalletFuture,
            characterRosterFuture,
            caddieRosterFuture,
            inventoryFuture,
            cardInventoryFuture,
            equipmentFuture,
            statisticsFuture,
            achievementsFuture
        )

        var progress = 1
        for (future in futures) {
            try {
                future.get(10, TimeUnit.SECONDS)
            } catch (e: Exception) {
                LOGGER.warn("Failed to process playerId $playerUid handover", e)
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
            cardInventory = cardInventoryFuture.get(),
            equipment = equipmentFuture.get(),
            statistics = statisticsFuture.get(),
            achievements = achievementsFuture.get()
        )

        // modify the pipeline
        val pipeline = channel.pipeline()
        pipeline.remove("handoverHandler")
        pipeline.remove("timeoutHandler")
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
        channel.write(CardholicReplies.inventory(player.cardInventory))
        channel.writeAndFlush(ServerChannelsPacket(gameServer.serverChannels))
    }
}
