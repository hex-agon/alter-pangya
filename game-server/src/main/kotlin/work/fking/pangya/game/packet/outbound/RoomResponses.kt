package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.model.Course
import work.fking.pangya.game.model.HoleMode
import work.fking.pangya.game.model.RoomType
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeFixedSizeString
import work.fking.pangya.networking.protocol.writePString

object RoomResponses {
    private const val RESPONSE_ID = 0x49
    private const val SETTINGS_ID = 0x4a
    private const val LEAVE_ID = 0x4c
    private const val CENSUS_ID = 0x48
    private const val SUCCESS = 0
    private const val ALREADY_STARTED = 8
    private const val CANNOT_CREATE = 18

    fun createSuccess(name: String, type: RoomType, id: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(RESPONSE_ID)
            buffer.writeShortLE(SUCCESS)
            buffer.writeFixedSizeString(name, 64)
            buffer.writeZero(25)
            buffer.writeShortLE(id)
            buffer.writeZero(111)
            buffer.writeIntLE(0)
            buffer.writeIntLE(0)
        }
    }

    fun leaveSuccess(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(LEAVE_ID)
            buffer.writeShortLE(-1) // new room id (-1 = lobby)
        }
    }

    fun roomInfo(roomType: RoomType, name: String, course: Course, holeMode: HoleMode, holeCount: Int, maxPlayers: Int, shotTime: Int, gameTime: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(SETTINGS_ID)
            buffer.writeShortLE(-1) // unknown constant -1
            buffer.writeByte(roomType.id()) // the room type
            buffer.writeByte(course.ordinal)
            buffer.writeByte(holeCount) // hole amount
            buffer.writeByte(holeMode.ordinal) // hole progression, 0 = normal, 1 = backwards, 2 = random start, 3 = shuffle

            // if mode == REPEAT then write byte (likely a boolean) & int as the repeating role
            if (holeMode == HoleMode.REPEAT) {
                buffer.writeByte(1)
            }
            buffer.writeIntLE(0) // natural wind, 0 disabled, 1 = enabled
            buffer.writeByte(maxPlayers) // max players
            buffer.writeZero(2)
            buffer.writeIntLE(shotTime) // shot time
            buffer.writeIntLE(gameTime) // game time
            buffer.writeIntLE(0)
            buffer.writeByte(1) // is room owner
            buffer.writePString(name)
        }
    }

    fun roomInitialCensus(player: Player): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(CENSUS_ID)
            buffer.writeByte(0) // initial room census
            buffer.writeShortLE(-1)
            buffer.writeByte(1) // player count
            buffer.writeIntLE(10) // connectionId
            buffer.writeFixedSizeString(player.nickname(), 22)
            buffer.writeFixedSizeString("", 17) // guildName
            buffer.writeByte(1) // room slot, starting at 1
            buffer.writeIntLE(0) // unknown
            buffer.writeIntLE(0) // title
            buffer.writeIntLE(player.equipment().equippedCharacterUid()) // character iff
            buffer.writeIntLE(0) // skin id background
            buffer.writeIntLE(0) // skin id frame
            buffer.writeIntLE(0) // skin id sticker
            buffer.writeIntLE(0) // skin id slot
            buffer.writeIntLE(0) // unknown
            buffer.writeIntLE(0) // duplicate skin id title
            buffer.writeShortLE(8) // unknown
            buffer.writeByte(player.rank())
            buffer.writeZero(3)
            buffer.writeByte(0)
            buffer.writeShortLE(0)
            buffer.writeIntLE(0) // guild id
            buffer.writeFixedSizeString("", 12)
            buffer.writeByte(0) // guild emblem id
            buffer.writeIntLE(player.uid())
            buffer.writeIntLE(0)
            buffer.writeShortLE(0)
            buffer.writeIntLE(0)

            // location for lounges
            buffer.writeFloatLE(0f) // unknown
            buffer.writeFloatLE(0f) // unknown
            buffer.writeFloatLE(0f) // unknown
            buffer.writeFloatLE(0f) // unknown

            // lounge shop
            buffer.writeIntLE(0) // shop active?
            buffer.writeFixedSizeString("", 64) // shop name
            buffer.writeIntLE(0) // mascot id
            buffer.writeFixedSizeString("", 22)
            buffer.writeZero(106)
            buffer.writeByte(0) // invited?
            buffer.writeFloatLE(0f) // avg score
            player.equippedCharacter().encode(buffer)
            // end room player entry
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

    fun loungePkt9e(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x9e)
            buffer.writeShortLE(0) // weather
            buffer.writeIntLE(1)
        }
    }

    fun alreadyStarted(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(RESPONSE_ID)
            buffer.writeByte(ALREADY_STARTED)
        }
    }

    fun cannotCreate(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(RESPONSE_ID)
            buffer.writeByte(CANNOT_CREATE)
        }
    }
}
