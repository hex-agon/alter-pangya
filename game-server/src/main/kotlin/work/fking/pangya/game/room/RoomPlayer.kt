package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.writeFixedSizeString

class RoomPlayer(
    val player: Player,
) {
    val connectionId: Int = player.connectionId

    var currentHole: Int = 1
    var finishedHole: Boolean = false

    fun write(message: Any) {
        player.write(message)
    }

    fun writeAndFlush(message: Any) {
        player.writeAndFlush(message)
    }

    fun flush() {
        player.flush()
    }

    fun encode(buffer: ByteBuf, extendedInfo: Boolean) {
        with(buffer) {
            writeIntLE(player.connectionId)
            writeFixedSizeString(player.nickname, 22)
            writeFixedSizeString("", 17) // guildName
            writeByte(1) // room slot, starting at 1
            writeIntLE(0) // unknown
            writeIntLE(0) // title
            writeIntLE(player.equipment.equippedCharacterUid) // character iff
            writeIntLE(0) // skin id background
            writeIntLE(0) // skin id frame
            writeIntLE(0) // skin id sticker
            writeIntLE(0) // skin id slot
            writeIntLE(0) // unknown
            writeIntLE(0) // duplicate skin id title
            writeShortLE(520) // room status (master, away, ready)
            writeByte(player.rank)
            writeShortLE(0x2560)
            writeIntLE(0) // guild id
            writeFixedSizeString("", 12)
            writeIntLE(player.uid)

            // lounge stuff
            writeIntLE(0) // animation/pose
            writeShortLE(0)
            writeIntLE(0) //

            // lounge location
            writeFloatLE(0f) // x
            writeFloatLE(0f) // y
            writeFloatLE(0f) // rotation

            // lounge shop
            writeIntLE(0) // shop active?
            writeFixedSizeString("", 64) // shop name

            writeIntLE(0) // mascot id
            writeShortLE(0) // item boost (pang mastery/pang nitro)

            writeIntLE(0)

            writeFixedSizeString("", 22) // ntreev nick?
            writeZero(105)
            writeByte(0) // invited?
            writeFloatLE(0f) // avg score
            writeFloatLE(0f)
        }

        if (extendedInfo) {
            player.equippedCharacter().encode(buffer)
        }
    }

    override fun toString(): String {
        return "RoomPlayer(player=${player.nickname})"
    }
}

enum class RoomPlayerFlags(
    private val flag: Int
) {
    RED_TEAM(1 shl 0),
    BLUE_TEAM(1 shl 1),
    AWAY(1 shl 2),
    MASTER(1 shl 3),
    FEMALE(1 shl 5),
    QUITTER_MEDIUM(1 shl 6), // quit rate between 31-41
    QUITTER_HIGH(1 shl 7), // quit rate 41+
    ANGEL(1 shl 8),// quit rate < 3
    READY(1 shl 9)
}