package work.fking.pangya.game.packet.handler.room

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.RoomEquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.RoomEquipmentUpdateReplies.EquipmentUpdateType
import work.fking.pangya.game.packet.outbound.RoomEquipmentUpdateReplies.EquipmentUpdateType.CADDIE
import work.fking.pangya.game.packet.outbound.RoomEquipmentUpdateReplies.EquipmentUpdateType.CHARACTER
import work.fking.pangya.game.packet.outbound.RoomEquipmentUpdateReplies.EquipmentUpdateType.CLUBSET
import work.fking.pangya.game.packet.outbound.RoomEquipmentUpdateReplies.EquipmentUpdateType.COMET
import work.fking.pangya.game.packet.outbound.RoomEquipmentUpdateReplies.EquipmentUpdateType.UNKNOWN
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.nullCaddie
import work.fking.pangya.game.room.RoomPlayer
import work.fking.pangya.networking.protocol.OutboundPacket
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(EquipmentUpdatePacketHandler::class.java)

class EquipmentUpdatePacketHandler(private val ack: Boolean) : ClientPacketHandler {
    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val typeId = packet.readUnsignedByte().toInt()
        val type = EquipmentUpdateType.entries.find { it.id == typeId } ?: throw IllegalStateException("Unsupported equipment update type=$typeId")

        when (type) {
            CADDIE -> handleUpdateCaddie(server, player, packet)
            COMET -> handleUpdateComet(server, player, packet)
            CLUBSET -> handleUpdateClubSet(server, player, packet)
            CHARACTER -> handleEquipCharacter(server, player, packet)
            UNKNOWN -> LOGGER.debug("Unknown equipment update \n{}", ByteBufUtil.hexDump(packet))
        }
    }

    private fun ackRoom(player: Player, packetFunc: (RoomPlayer) -> OutboundPacket) {
        val room = player.currentRoom ?: throw IllegalStateException("Player is not in a room")
        val roomPlayer = room.findSelf(player)
        room.broadcast(packetFunc.invoke(roomPlayer))
    }

    private fun handleUpdateCaddie(server: GameServer, player: Player, buffer: ByteBuf) {
        val caddieUid = buffer.readIntLE()
        val caddie = if (caddieUid == 0) {
            nullCaddie()
        } else {
            player.caddieRoster.findByUid(caddieUid) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a caddie it does not own ($caddieUid)")
        }

        player.equipment.equipCaddie(caddie)
        if (ack) {
            ackRoom(player) { roomPlayer -> RoomEquipmentUpdateReplies.ack(CADDIE, roomPlayer) { caddie.encode(it) } }
        }

        server.runTask {
            try {
                server.persistenceCtx.noTx { tx ->
                    equipmentRepository.save(tx, player.uid, player.equipment)
                }
            } catch (e: SQLException) {
                LOGGER.error("Failed to update player (${player.username}) equipped caddie", e)
            }
        }
    }

    private fun handleUpdateComet(server: GameServer, player: Player, buffer: ByteBuf) {
        val cometIffId = buffer.readIntLE()
        val comet = player.inventory.findByIffId(cometIffId) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a comet it does not own ($cometIffId)")

        player.equipment.equipComet(comet)
        if (ack) {
            ackRoom(player) { roomPlayer -> RoomEquipmentUpdateReplies.ack(COMET, roomPlayer) { it.writeIntLE(comet.iffId) } }
        }
        server.runTask {
            try {
                server.persistenceCtx.noTx { tx ->
                    equipmentRepository.save(tx, player.uid, player.equipment)
                }
            } catch (e: SQLException) {
                LOGGER.error("Failed to update player (${player.username}) equipped comet", e)
            }
        }
    }

    private fun handleUpdateClubSet(server: GameServer, player: Player, buffer: ByteBuf) {
        val clubSetUid = buffer.readIntLE()
        val clubSet = player.inventory.findByUid(clubSetUid) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a clubSet it does not own ($clubSetUid)")

        player.equipment.equipClubSet(clubSet)
        if (ack) {
            ackRoom(player) { roomPlayer -> RoomEquipmentUpdateReplies.ack(CLUBSET, roomPlayer) { packet ->
                packet.writeIntLE(clubSet.uid)
                packet.writeIntLE(clubSet.iffId)
                clubSet.stats.forEach { packet.writeShortLE(it) }
                repeat(5) { packet.writeShortLE(0) }
            } }
        }
        server.runTask {
            try {
                server.persistenceCtx.noTx { tx ->
                    equipmentRepository.save(tx, player.uid, player.equipment)
                }
            } catch (e: SQLException) {
                LOGGER.error("Failed to update player (${player.username}) equipped clubSet", e)
            }
        }
    }

    private fun handleEquipCharacter(server: GameServer, player: Player, buffer: ByteBuf) {
        val characterUid = buffer.readIntLE()
        val character = player.characterRoster.findByUid(characterUid) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a character it does not own ($characterUid)")

        player.equipment.equipCharacter(character)
        if (ack) {
            ackRoom(player) { roomPlayer -> RoomEquipmentUpdateReplies.ack(CHARACTER, roomPlayer) { character.encode(it) } }
        }
        server.runTask {
            try {
                server.persistenceCtx.noTx { tx ->
                    equipmentRepository.save(tx, player.uid, player.equipment)
                }
            } catch (e: SQLException) {
                LOGGER.error("Failed to update player (${player.username}) equipped character", e)
            }
        }
    }
}