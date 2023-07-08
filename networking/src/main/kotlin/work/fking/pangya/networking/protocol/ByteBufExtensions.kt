package work.fking.pangya.networking.protocol

import io.netty.buffer.ByteBuf
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

fun ByteBuf.readPString(): String {
    val size = this.readUnsignedShortLE()
    val bytes = ByteArray(size)
    readBytes(bytes)
    return String(bytes, StandardCharsets.US_ASCII)
}

fun ByteBuf.readPStringCharArray(): CharArray {
    val size = readUnsignedShortLE()
    val characters = CharArray(size)
    for (i in 0 until size) {
        characters[i] = readByte().toInt().toChar()
    }
    return characters
}

fun ByteBuf.readFixedSizeString(size: Int): String {
    val temp = ByteArray(size)
    readBytes(temp)
    return String(temp, 0, temp.indexOf(0), StandardCharsets.US_ASCII)
}

fun ByteBuf.writePString(string: String) {
    val bytes = string.toByteArray(StandardCharsets.US_ASCII)
    writeShortLE(bytes.size)
    writeBytes(bytes)
}

fun ByteBuf.writeFixedSizeString(string: String, size: Int) {
    val bytes = string.toByteArray(StandardCharsets.US_ASCII)
    writeBytes(bytes.copyOf(size))
}

fun ByteBuf.writeLocalDateTime(localDateTime: LocalDateTime) {
    writeShortLE(localDateTime.year)
    writeShortLE(localDateTime.monthValue)
    writeShortLE(localDateTime.dayOfWeek.value)
    writeShortLE(localDateTime.dayOfMonth)
    writeShortLE(localDateTime.hour)
    writeShortLE(localDateTime.minute)
    writeShortLE(localDateTime.second)
    writeShortLE(0)
}