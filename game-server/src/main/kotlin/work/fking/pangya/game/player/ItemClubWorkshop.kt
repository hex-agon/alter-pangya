package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf

class ItemClubWorkshop(
    val stats: IntArray = IntArray(5),
    val mastery: Int,
    val usedRecoveryPoints: Int,
    val level: Int,
    val rank: Int
)

fun ByteBuf.write(clubWorkshop: ItemClubWorkshop) {
    with(clubWorkshop) {
        writeShortLE(0) // unknown
        stats.forEach { writeShortLE(it) }
        writeIntLE(mastery)
        writeIntLE(usedRecoveryPoints)
        writeIntLE(level) // level? client seems to completely ignore it and just compute it
        writeIntLE(rank) // rank? client seems to completely ignore it and just compute it
    }
}

fun nullItemClubWorkshop() = ItemClubWorkshop(
    mastery = 0,
    usedRecoveryPoints = 0,
    level = 0,
    rank = 0
)