package work.fking.pangya.game.room.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.Coord2D
import work.fking.pangya.game.model.Coord3D
import work.fking.pangya.game.model.write

data class TourneyShotData(
    val unk1: Float,
    val unk2: Coord3D,
    val unk3: Int,
    val unk4: Coord3D,
    val unk5: Coord3D,
    val unk6: Coord2D,
    val unk7: Int,
    val spin: Float,
    val curve: Float,
    val unk8: Int,
    val clubType: Int,
    val unk9: Float,
    val unk10: Float,
    val unk11: Float,
    val unk12: Float,
    val unk13: Float,
    val unk14: Int
) {
    constructor(buffer: ByteBuf) : this(
        unk1 = buffer.readFloatLE(),
        unk2 = Coord3D(buffer),
        unk3 = buffer.readUnsignedByte().toInt(),
        unk4 = Coord3D(buffer),
        unk5 = Coord3D(buffer),
        unk6 = Coord2D(buffer),
        unk7 = buffer.readIntLE(),
        spin = buffer.readFloatLE(),
        curve = buffer.readFloatLE(),
        unk8 = buffer.readByte().toInt(),
        clubType = buffer.readByte().toInt(),
        unk9 = buffer.readFloatLE(),
        unk10 = buffer.readFloatLE(),
        unk11 = buffer.readFloatLE(),
        unk12 = buffer.readFloatLE(),
        unk13 = buffer.readFloatLE(),
        unk14 = buffer.readIntLE()
    )

    fun serialize(buffer: ByteBuf) {
        buffer.writeFloatLE(unk1)
        buffer.write(unk2)
        buffer.writeByte(unk3)
        buffer.write(unk4)
        buffer.write(unk5)
        buffer.write(unk6)
        buffer.writeIntLE(unk7)
        buffer.writeFloatLE(spin)
        buffer.writeFloatLE(curve)
        buffer.writeByte(unk8)
        buffer.writeByte(clubType)
        buffer.writeFloatLE(unk9)
        buffer.writeFloatLE(unk10)
        buffer.writeFloatLE(unk11)
        buffer.writeFloatLE(unk12)
        buffer.writeFloatLE(unk13)
        buffer.writeIntLE(unk14)
    }
}