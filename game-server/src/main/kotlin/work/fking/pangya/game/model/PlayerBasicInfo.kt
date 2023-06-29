package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.writeFixedSizeString

class PlayerBasicInfo {

    fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0xFFFF) // roomId
        buffer.writeFixedSizeString("hexagon", 22) // username
        buffer.writeFixedSizeString("Hex agon", 22) // nickname
        buffer.writeFixedSizeString("guildname", 17)
        buffer.writeFixedSizeString("guildimg", 24)
        buffer.writeIntLE(10) // connection id?
        buffer.writeZero(12)
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
        buffer.writeShortLE(0)
        buffer.writeZero(6)
        buffer.writeZero(16)
        buffer.writeFixedSizeString("guildimg", 128)
        buffer.writeIntLE(1335) // player id
    }
}
