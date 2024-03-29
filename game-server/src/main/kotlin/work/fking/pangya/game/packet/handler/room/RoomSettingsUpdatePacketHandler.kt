package work.fking.pangya.game.packet.handler.room

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.RoomArtifactChange
import work.fking.pangya.game.room.RoomCourseChange
import work.fking.pangya.game.room.RoomGameTimeChange
import work.fking.pangya.game.room.RoomHoleCountChange
import work.fking.pangya.game.room.RoomHoleModeChange
import work.fking.pangya.game.room.RoomHoleRepeatFixedHoleChange
import work.fking.pangya.game.room.RoomHoleRepeatHoleChange
import work.fking.pangya.game.room.RoomNameChange
import work.fking.pangya.game.room.RoomNaturalWindChange
import work.fking.pangya.game.room.RoomPasswordChange
import work.fking.pangya.game.room.RoomPlayerCountChange
import work.fking.pangya.game.room.RoomShotTimeChange
import work.fking.pangya.game.room.RoomTypeChange
import work.fking.pangya.game.room.RoomUpdate
import work.fking.pangya.game.room.courseById
import work.fking.pangya.game.room.holeModeById
import work.fking.pangya.game.room.roomTypeById
import work.fking.pangya.networking.protocol.readPString
import java.time.Duration
import java.time.temporal.ChronoUnit

class RoomSettingsUpdatePacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val room = player.currentRoom ?: throw IllegalStateException("${player.nickname} attempted to update a room without being in one")

        val unknown = packet.readUnsignedShortLE()
        val count = packet.readUnsignedByte()
        val updates = ArrayList<RoomUpdate>(count.toInt())

        repeat(count.toInt()) {
            val type = packet.readUnsignedByte()
            println(ByteBufUtil.prettyHexDump(packet))
            val roomUpdate = when (type.toInt()) {
                0 -> RoomNameChange(packet.readPString())
                1 -> RoomPasswordChange(packet.readPString())
                2 -> RoomTypeChange(roomTypeById(packet.readByte()))
                3 -> RoomCourseChange(courseById(packet.readByte()))
                4 -> RoomHoleCountChange(packet.readByte().toInt())
                5 -> RoomHoleModeChange(holeModeById(packet.readByte()))
                6 -> RoomShotTimeChange(Duration.of(packet.readUnsignedByte().toLong(), ChronoUnit.SECONDS))
                7 -> RoomPlayerCountChange(packet.readUnsignedByte().toInt())
                8 -> RoomGameTimeChange(Duration.of(packet.readUnsignedByte().toLong(), ChronoUnit.MINUTES))
                11 -> RoomHoleRepeatHoleChange(packet.readUnsignedByte().toInt())
                12 -> RoomHoleRepeatFixedHoleChange(packet.readIntLE() == 7)
                13 -> RoomArtifactChange(packet.readInt())
                14 -> RoomNaturalWindChange(packet.readInt() == 0x1000000)
                else -> throw IllegalStateException("Unsupported room setting change type=$type")
            }
            updates.add(roomUpdate)
        }
        room.handleUpdates(updates)
    }
}