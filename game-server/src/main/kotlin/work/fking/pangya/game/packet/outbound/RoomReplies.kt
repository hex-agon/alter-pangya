package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.room.Room
import work.fking.pangya.game.room.RoomPlayer
import work.fking.pangya.game.room.write
import work.fking.pangya.networking.protocol.OutboundPacket

object RoomReplies {
    private const val PACKET_LIST = 0x47
    private const val PACKET_PLAYER_CENSUS = 0x48
    private const val PACKET_JOIN = 0x49
    private const val PACKET_ROOM_SETTINGS = 0x4a
    private const val PACKET_LEAVE = 0x4c

    private const val CENSUS_LIST = 0
    private const val CENSUS_ADD = 1
    private const val CENSUS_REMOVE = 2
    private const val CENSUS_UPDATE = 3

    private const val JOIN_SUCCESS = 0
    private const val JOIN_ALREADY_STARTED = 8
    private const val JOIN_CANNOT_CREATE = 18

    fun list(rooms: List<Room>): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_LIST)
            buffer.writeByte(rooms.size)
            buffer.writeByte(0)

            buffer.writeShortLE(-1)
            rooms.forEach { buffer.write(it) }
        }
    }

    fun joinAlreadyStarted(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_JOIN)
            buffer.writeByte(JOIN_ALREADY_STARTED)
        }
    }

    fun joinCannotCreate(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_JOIN)
            buffer.writeByte(JOIN_CANNOT_CREATE)
        }
    }

    fun joinAck(room: Room): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_JOIN)
            buffer.writeShortLE(JOIN_SUCCESS)
            buffer.write(room)
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
            buffer.write(room.settings)
        }
    }

    fun roomCensusList(roomPlayers: List<RoomPlayer>, roomOwner: Boolean, extendedInfo: Boolean): OutboundPacket {
        // it seems like this packet can be chunked
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_PLAYER_CENSUS)
            buffer.writeByte(CENSUS_LIST)
            buffer.writeShortLE(-1)
            buffer.writeByte(roomPlayers.size)
            roomPlayers.forEach { it.encode(buffer, roomOwner, extendedInfo) }
            buffer.writeByte(0)
        }
    }

    fun roomCensusAdd(player: RoomPlayer, roomOwner: Boolean, extendedInfo: Boolean): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_PLAYER_CENSUS)
            buffer.writeByte(CENSUS_ADD)
            buffer.writeShortLE(-1)
            buffer.writeByte(1)
            player.encode(buffer, roomOwner, extendedInfo)
        }
    }

    fun roomCensusRemove(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_PLAYER_CENSUS)
            buffer.writeByte(CENSUS_REMOVE)
            buffer.writeShortLE(-1)
            buffer.writeIntLE(player.connectionId)
        }
    }

    fun roomCensusUpdate(player: RoomPlayer, roomOwner: Boolean, extendedInfo: Boolean): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_PLAYER_CENSUS)
            buffer.writeByte(CENSUS_UPDATE)
            buffer.writeIntLE(player.connectionId) // intentionally duplicated
            player.encode(buffer, roomOwner, extendedInfo)
            buffer.writeByte(0)
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

    fun loungeUpdateWeather(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x9e)
            buffer.writeShortLE(0) // weather
            buffer.writeIntLE(1)
        }
    }
}
