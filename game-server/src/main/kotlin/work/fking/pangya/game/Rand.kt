package work.fking.pangya.game

import java.security.SecureRandom
import java.util.Random

object Rand {
    private val RANDOM: Random = SecureRandom()

    fun randomHexString(length: Int): String {
        val bytes = ByteArray(length)
        RANDOM.nextBytes(bytes)
        val builder = StringBuilder()
        for (aByte in bytes) {
            builder.append(String.format("%02x", aByte))
        }
        return builder.toString()
    }

    fun nextInt(): Int {
        return RANDOM.nextInt()
    }

    fun nextLong(): Long {
        return RANDOM.nextLong()
    }

    fun max(maximum: Int): Int {
        return between(0, maximum - 1)
    }

    fun maxInclusive(maximum: Int): Int {
        return between(0, maximum)
    }

    fun between(minimum: Int, maximum: Int): Int {
        require(minimum <= maximum) { "The minimum value must be less than the maximum" }
        return minimum + RANDOM.nextInt(maximum - minimum + 1)
    }
}
