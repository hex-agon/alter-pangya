package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.RoomPlayerFlag.AWAY
import work.fking.pangya.game.room.RoomPlayerFlag.MASTER
import work.fking.pangya.game.room.RoomPlayerFlag.READY
import work.fking.pangya.networking.protocol.writeFixedSizeString

class RoomPlayer(
    val player: Player,
    var slot: Int
) {
    val uid: Int = player.uid
    val connectionId: Int = player.connectionId

    // lobby state
    var ready: Boolean = false
    var away: Boolean = false

    // in game state
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

    private fun computeRoomFlags(roomOwner: Boolean): List<RoomPlayerFlag> {
        val playerFlags = mutableListOf<RoomPlayerFlag>()

        if (roomOwner) {
            playerFlags += MASTER
        }

        if (ready) {
            playerFlags += AWAY
        }

        if (ready) {
            playerFlags += READY
        }
        return playerFlags
    }

    fun encode(buffer: ByteBuf, roomOwner: Boolean, extendedInfo: Boolean) {

        with(buffer) {
            writeIntLE(player.connectionId)
            writeFixedSizeString(player.nickname, 22)
            writeFixedSizeString("", 17) // guildName
            writeByte(slot)
            writeIntLE(0) // unknown
            writeIntLE(0) // title
            writeIntLE(player.equipment.equippedCharacterUid) // character iff
            writeIntLE(0) // skin id background
            writeIntLE(0) // skin id frame
            writeIntLE(0) // skin id sticker
            writeIntLE(0) // skin id slot
            writeIntLE(0) // unknown
            writeIntLE(0) // duplicate skin id title
            writeShortLE(pack(computeRoomFlags(roomOwner))) // room status (master, away, ready)
            writeByte(player.rank)
            writeShortLE(0x2560)
            writeIntLE(0) // guild id
            writeFixedSizeString("", 12) // guildmark
            writeIntLE(player.uid)

            // lounge stuff
            writeIntLE(0) // animation/pose
            writeShortLE(0)
            writeIntLE(0)

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

private fun pack(flags: List<RoomPlayerFlag>): Int {
    var pack = 0;
    for (flag in flags) {
        pack = pack or flag.flag
    }
    return pack;
}

enum class RoomPlayerFlag(
    val flag: Int
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