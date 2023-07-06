package work.fking.pangya.networking.protocol

import io.netty.buffer.ByteBuf
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.temporal.ChronoField

fun ByteBuf.readPString(): String {
    val size = this.readUnsignedShortLE()
    val bytes = ByteArray(size)
    this.readBytes(bytes)
    return String(bytes, StandardCharsets.US_ASCII)
}

fun ByteBuf.readPStringCharArray(): CharArray {
    val size = this.readUnsignedShortLE()
    val characters = CharArray(size)
    for (i in 0 until size) {
        characters[i] = this.readByte().toInt().toChar()
    }
    return characters
}

fun ByteBuf.readFixedSizeString(size: Int): String {
    val temp = ByteArray(size)
    this.readBytes(temp)
    return String(temp, 0, temp.indexOf(0), StandardCharsets.US_ASCII)
}

fun ByteBuf.writePString(string: String) {
    val bytes = string.toByteArray(StandardCharsets.US_ASCII)
    this.writeShortLE(bytes.size)
    this.writeBytes(bytes)
}

fun ByteBuf.writeFixedSizeString(string: String, size: Int) {
    val bytes = string.toByteArray(StandardCharsets.US_ASCII)
    this.writeBytes(bytes.copyOf(size))
}

fun ByteBuf.writeLocalDateTime(localDateTime: LocalDateTime) {
    this.writeShortLE(localDateTime.year)
    this.writeShortLE(localDateTime.monthValue)
    this.writeShortLE(localDateTime.dayOfWeek.value)
    this.writeShortLE(localDateTime.dayOfMonth)
    this.writeShortLE(localDateTime.hour)
    this.writeShortLE(localDateTime.minute)
    this.writeShortLE(localDateTime.second)
    this.writeShortLE(0)
}