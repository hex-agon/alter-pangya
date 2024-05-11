package work.fking.pangya.game.packet.handler.shop

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.model.IffType
import work.fking.pangya.game.model.iffTypeFromId
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.PurchaseFailedReason.ALREADY_HAVE_THAT_ITEM
import work.fking.pangya.game.packet.outbound.ShopReplies
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.task.ShopUnlockCharacterTask

private val LOGGER = LoggerFactory.getLogger(ShopPurchaseHandler::class.java)

class ShopPurchaseHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        println(ByteBufUtil.prettyHexDump(packet))
        val rental = packet.readByte()
        val numOfItems = packet.readUnsignedShortLE()

        repeat(numOfItems) {
            val uid = packet.readIntLE()
            val iffId = packet.readIntLE()
            val rentalTime = packet.readUnsignedShortLE()
            val unknown = packet.readUnsignedShortLE()
            val quantity = packet.readIntLE()
            val pangCost = packet.readIntLE()
            val cookieCost = packet.readIntLE()
            val couponId = packet.readIntLE()

            when (iffTypeFromId(iffId)) {
                IffType.CHARACTER -> handleCharacterPurchase(server, player, iffId)
                else -> handleUnknownPurchase(player, iffId)
            }
        }
    }

    private fun handleCharacterPurchase(server: GameServer, player: Player, iffId: Int) {
        if (player.characterRoster.findByIffId(iffId) != null) {
            player.writeAndFlush(ShopReplies.purchaseFailed(ALREADY_HAVE_THAT_ITEM))
            return
        }

        server.runTask(
            ShopUnlockCharacterTask(
                server.persistenceCtx,
                player,
                iffId
            )
        )
    }

    private fun handleUnknownPurchase(player: Player, iffId: Int) {
        LOGGER.warn("Player tried to purchase iffId={} type={} but we don't know how to handle it", iffId, iffTypeFromId(iffId))
        player.writeAndFlush(ShopReplies.purchaseFailed())
    }
}