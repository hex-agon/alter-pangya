package work.fking.pangya.common

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

    /**
     * Returns a value between zero and the given max value.
     *
     * @param maximum The maximum value, exclusive.
     * @return A value between zero and the max value.
     */
    fun max(maximum: Int): Int {
        return between(0, maximum - 1)
    }

    /**
     * Returns a value between zero and the given max value.
     *
     * @param maximum The maximum value, inclusive.
     * @return A value between zero and the max value.
     */
    fun maxInclusive(maximum: Int): Int {
        return between(0, maximum)
    }

    /**
     * Returns a pseudo-random value between the given bounds.
     *
     * @param minimum The minimum value, inclusive.
     * @param maximum The maximum value, inclusive.
     * @return The random value.
     */
    fun between(minimum: Int, maximum: Int): Int {
        require(minimum <= maximum) { "The minimum value must be less than the maximum" }
        return minimum + RANDOM.nextInt(maximum - minimum + 1)
    }
}
