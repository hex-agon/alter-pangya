package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.writeFixedSizeString

class RoomPlayer(
    val player: Player,
) {
    val connectionId: Int = player.connectionId

    fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(player.connectionId)
        buffer.writeFixedSizeString(player.nickname, 22)
        buffer.writeFixedSizeString("", 17) // guildName
        buffer.writeByte(1) // room slot, starting at 1
        buffer.writeIntLE(0) // unknown
        buffer.writeIntLE(0) // title
        buffer.writeIntLE(player.equipment.equippedCharacterUid()) // character iff
        buffer.writeIntLE(0) // skin id background
        buffer.writeIntLE(0) // skin id frame
        buffer.writeIntLE(0) // skin id sticker
        buffer.writeIntLE(0) // skin id slot
        buffer.writeIntLE(0) // unknown
        buffer.writeIntLE(0) // duplicate skin id title
        buffer.writeShortLE(8) // room status (master, away, ready)
        buffer.writeByte(player.rank)
        buffer.writeShortLE(0)
        buffer.writeIntLE(0) // guild id
        buffer.writeFixedSizeString("", 12)
        buffer.writeIntLE(player.uid)

        // lounge stuff
        buffer.writeIntLE(0) // animation/pose
        buffer.writeShortLE(0)
        buffer.writeIntLE(0) //

        // lounge location
        buffer.writeFloatLE(0f) // x
        buffer.writeFloatLE(0f) // y
        buffer.writeFloatLE(0f) // rotation

        // lounge shop
        buffer.writeIntLE(0) // shop active?
        buffer.writeFixedSizeString("", 64) // shop name

        buffer.writeIntLE(0) // mascot id
        buffer.writeShortLE(0) // item boost (pang mastery/pang nitro)

        buffer.writeIntLE(0)

        buffer.writeFixedSizeString("", 22) // ntreev nick?
        buffer.writeZero(105)
        buffer.writeByte(0) // invited?
        buffer.writeFloatLE(0f) // avg score
        buffer.writeFloatLE(0f)
        player.equippedCharacter().encode(buffer)
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