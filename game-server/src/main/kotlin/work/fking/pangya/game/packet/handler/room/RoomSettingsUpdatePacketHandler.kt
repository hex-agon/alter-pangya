package work.fking.pangya.game.packet.handler.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.Course
import work.fking.pangya.game.room.HoleMode
import work.fking.pangya.game.room.RoomArtifactChange
import work.fking.pangya.game.room.RoomCourseChange
import work.fking.pangya.game.room.RoomGameTimeChange
import work.fking.pangya.game.room.RoomHoleCountChange
import work.fking.pangya.game.room.RoomHoleModeChange
import work.fking.pangya.game.room.RoomNameChange
import work.fking.pangya.game.room.RoomNaturalWindChange
import work.fking.pangya.game.room.RoomPasswordChange
import work.fking.pangya.game.room.RoomPlayerCountChange
import work.fking.pangya.game.room.RoomShotTimeChange
import work.fking.pangya.game.room.RoomType
import work.fking.pangya.game.room.RoomTypeChange
import work.fking.pangya.game.room.RoomUpdate
import work.fking.pangya.game.room.courseById
import work.fking.pangya.game.room.holeModeById
import work.fking.pangya.networking.protocol.readPString

class RoomSettingsUpdatePacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val room = player.currentRoom ?: throw IllegalStateException("${player.nickname} attempted to update a room without being in one")

        val unknown = packet.readUnsignedShortLE()
        val count = packet.readUnsignedByte()
        val updates = ArrayList<RoomUpdate>(count.toInt())

        repeat(count.toInt()) {
            val type = packet.readUnsignedByte()
            val roomUpdate = when (type.toInt()) {
                0 -> RoomNameChange(packet.readPString())
                1 -> RoomPasswordChange(packet.readPString())
                2 -> RoomTypeChange(RoomType.forId(packet.readByte()))
                3 -> RoomCourseChange(courseById(packet.readByte()))
                4 -> RoomHoleCountChange(packet.readByte().toInt())
                5 -> RoomHoleModeChange(holeModeById(packet.readByte()))
                6 -> RoomShotTimeChange(packet.readUnsignedByte().toInt() * 1000) // client sends in seconds, need ms
                7 -> RoomPlayerCountChange(packet.readUnsignedByte().toInt())
                8 -> RoomGameTimeChange(packet.readUnsignedByte().toInt() * 60 * 1000) // client sends in minutes, need ms
                13 -> RoomArtifactChange(packet.readInt())
                14 -> RoomNaturalWindChange(packet.readInt() == 0x1000000)
                else -> throw IllegalStateException("Unsupported room setting change type=$type")
            }
            updates.add(roomUpdate)
        }
        room.handleUpdates(updates)
    }
}