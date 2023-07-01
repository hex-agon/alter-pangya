package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.room.Room
import work.fking.pangya.game.room.RoomPlayer
import work.fking.pangya.networking.protocol.OutboundPacket

object RoomReplies {
    private const val PACKET_JOIN = 0x49
    private const val PACKET_ROOM_SETTINGS = 0x4a
    private const val PACKET_LEAVE = 0x4c
    private const val PACKET_CENSUS = 0x48

    private const val CENSUS_LIST = 0
    private const val CENSUS_ADD = 1
    private const val CENSUS_REMOVE = 2
    private const val CENSUS_UPDATE = 3

    private const val JOIN_SUCCESS = 0
    private const val JOIN_ALREADY_STARTED = 8
    private const val JOIN_CANNOT_CREATE = 18

    fun joinAck(room: Room): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_JOIN)
            buffer.writeShortLE(JOIN_SUCCESS)
            room.encodeInfo(buffer)
        }
    }

    fun leaveAck(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_LEAVE)
            buffer.writeShortLE(-1) // new room id (-1 = lobby)
        }
    }

    fun roomSettings(room: Room): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_ROOM_SETTINGS)
            buffer.writeShortLE(-1)
            room.encodeSettings(buffer)
        }
    }

    fun roomCensusList(roomPlayers: List<RoomPlayer>): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_CENSUS)
            buffer.writeByte(CENSUS_LIST)
            buffer.writeShortLE(-1)
            buffer.writeByte(roomPlayers.size)
            roomPlayers.forEach { it.encode(buffer) }
            buffer.writeByte(0)
        }
    }

    fun roomCensusAdd(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_CENSUS)
            buffer.writeByte(CENSUS_ADD)
            player.encode(buffer)
        }
    }

    fun roomCensusRemove(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_CENSUS)
            buffer.writeByte(CENSUS_REMOVE)
            buffer.writeIntLE(player.connectionId)
        }
    }

    fun roomCensusUpdate(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_CENSUS)
            buffer.writeByte(CENSUS_UPDATE)
            buffer.writeIntLE(player.connectionId) // intentionally duplicated
            player.encode(buffer)
        }
    }

    fun loungePkt196(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x196)
            buffer.writeIntLE(1)
            buffer.writeFloatLE(1f)
            buffer.writeFloatLE(1f)
            buffer.writeFloatLE(1f)
            buffer.writeFloatLE(1f)
        }
    }

    fun loungePkt9e(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x9e)
            buffer.writeShortLE(0) // weather
            buffer.writeIntLE(1)
        }
    }

    fun alreadyStarted(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_JOIN)
            buffer.writeByte(JOIN_ALREADY_STARTED)
        }
    }

    fun cannotCreate(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_JOIN)
            buffer.writeByte(JOIN_CANNOT_CREATE)
        }
    }
}
