package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.writeFixedSizeString

class PlayerBasicInfo {

    fun encode(buffer: ByteBuf, player: Player) {
        buffer.writeShortLE(0xFFFF) // roomId
        buffer.writeFixedSizeString(player.username, 22) // username
        buffer.writeFixedSizeString(player.nickname, 22) // nickname
        buffer.writeFixedSizeString("guildname", 17)
        buffer.writeFixedSizeString("guildimg", 24)
        buffer.writeIntLE(player.connectionId)
        buffer.writeZero(12)
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
        buffer.writeShortLE(0)
        buffer.writeZero(6)
        buffer.writeZero(16)
        buffer.writeFixedSizeString("globalid", 128)
        buffer.writeIntLE(player.uid)
    }
}
