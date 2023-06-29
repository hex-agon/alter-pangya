package work.fking.pangya.networking.crypt

import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import io.netty.buffer.Unpooled.EMPTY_BUFFER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import work.fking.pangya.networking.crypt.PangCrypt.PangCryptException
import work.fking.pangya.networking.crypt.PangCrypt.decrypt
import work.fking.pangya.networking.crypt.PangCrypt.encrypt

/**
 * Test cases taken from:
 * https://github.com/pangbox/pangcrypt/blob/master/client_test.go
 * https://github.com/pangbox/pangcrypt/blob/master/server_test.go
 */
@TestInstance(Lifecycle.PER_CLASS)
class TestPangCrypt {
    @Test
    fun test_decrypt_key_out_of_bounds() {
        assertThrows(PangCryptException::class.java) { decrypt(EMPTY_BUFFER, 18) }
        assertThrows(PangCryptException::class.java) { decrypt(EMPTY_BUFFER, -1) }
    }

    @Test
    fun test_decrypt_buf_too_small() {
        assertThrows(PangCryptException::class.java) { decrypt(EMPTY_BUFFER, 1) }
    }

    @Test
    fun test_decrypt_key0() {
        val encrypted = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("34220000F0000002C6006867746C040A0E4C1B00054C13526D6C64341F31323E2E011C070001"))
        val expected = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("0000020D006865796C6C6F7720776F726C643D1F000009003132372E302E302E31"))
        decrypt(encrypted, 0)
        assertEquals(ByteBufUtil.hexDump(expected), ByteBufUtil.hexDump(encrypted))
    }

    @Test
    fun test_decrypt_key5() {
        val encrypted = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("073c00005b010004476b6f6c6e4a6f58571846067b7b0202747175700505020772737677047c7606730a04700274014234463600000000000000000000000000"))
        val expected = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("010004006a6f686e200030393846364243443436323144333733434144453445383332363237423446360000000000000000000000000000000000"))
        decrypt(encrypted, 5)
        assertEquals(ByteBufUtil.hexDump(expected), ByteBufUtil.hexDump(encrypted))
    }

    @Test
    fun test_decrypt_key5_2() {
        val encrypted = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("110700006e0300eacf0300"))
        val expected = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("0300ea4e0000"))
        decrypt(encrypted, 5)
        assertEquals(ByteBufUtil.hexDump(expected), ByteBufUtil.hexDump(encrypted))
    }

    @Test
    fun test_encrypt_key_out_of_bounds() {
        assertThrows(PangCryptException::class.java) { encrypt(EMPTY_BUFFER, EMPTY_BUFFER, 0, 18, 0) }
        assertThrows(PangCryptException::class.java) { encrypt(EMPTY_BUFFER, EMPTY_BUFFER, 0, -1, 0) }
    }

    @Test
    fun test_encrypt_salt_out_of_bounds() {
        assertThrows(PangCryptException::class.java) { encrypt(EMPTY_BUFFER, EMPTY_BUFFER, 0, 0, 256) }
        assertThrows(PangCryptException::class.java) { encrypt(EMPTY_BUFFER, EMPTY_BUFFER, 0, 0, -1) }
    }

    @Test
    fun test_encrypt_key0() {
        val compressed = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("1b9600ffe0f50500000000110000"))
        val expected = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("EE1300FB000000361B9600F5FB6305FFE0F505110000"))
        val target = Unpooled.buffer()
        encrypt(target, compressed, 10, 0, 0xEE)
        assertEquals(ByteBufUtil.hexDump(expected), ByteBufUtil.hexDump(target))
    }

    @Test
    fun test_encrypt_key7() {
        val compressed = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("1801000100000000110000"))
        val expected = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("49100020000000881801000618010001110000"))
        val target = Unpooled.buffer()
        encrypt(target, compressed, 7, 7, 0x49)
        assertEquals(ByteBufUtil.hexDump(expected), ByteBufUtil.hexDump(target))
    }

    @Test
    fun test_encrypt_key5() {
        val compressed = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("0901000004006a6f686eb410002d00000908006a6f686e746573740000110000"))
        val expected = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("8425004500000058090100270d016a6f6c6ede7f6843b4100925006a66606e1e0a1b1a7465627400"))
        val target = Unpooled.buffer()
        encrypt(target, compressed, 39, 5, 0x84)
        assertEquals(ByteBufUtil.hexDump(expected), ByteBufUtil.hexDump(target))
    }
}
