package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.player.PlayerAchievements
import work.fking.pangya.networking.protocol.OutboundPacket

object AchievementReplies {

    fun achievementList(playerAchievements: PlayerAchievements): List<OutboundPacket> {
        // MAX 20 per entry
        return playerAchievements.entries.chunked(20)
            .map { entries ->
                OutboundPacket { buffer ->
                    buffer.writeShortLE(0x22d) // achievement response
                    buffer.writeIntLE(0) // success/error?

                    buffer.writeIntLE(playerAchievements.entries.size)
                    buffer.writeIntLE(entries.size)

                    for (entry in entries) {
                        entry.encode(buffer)
                    }
                }
            }
    }

    fun achievementsOk(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x22c) // achievement response
            buffer.writeIntLE(0) // success/error?
        }
    }
}