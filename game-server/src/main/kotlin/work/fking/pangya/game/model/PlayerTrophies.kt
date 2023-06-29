package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf

class PlayerTrophies {
    fun encode(buffer: ByteBuf) {
        // Starting from Amateur 6 to Amateur 1 repeat qualities gold, silver, bronze
        // Then from pro 1 to pro 7 repeat qualities gold, silver bronze
        buffer.writeShortLE(1)
        buffer.writeZero(76)
    }
}
