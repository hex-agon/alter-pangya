package work.fking.common;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Random;

@UtilityClass
public class Rand {

    private static final Random RANDOM = new SecureRandom();

    public static long nextLong() {
        return RANDOM.nextLong();
    }

    /**
     * Returns a value between zero and the given max value.
     *
     * @param maximum The maximum value, inclusive.
     * @return A value between zero and the max value.
     */
    public static int withMax(int maximum) {
        return between(0, maximum);
    }

    /**
     * Returns a pseudo-random value between the given bounds.
     *
     * @param minimum The minimum value, inclusive.
     * @param maximum The maximum value, inclusive.
     * @return The random value.
     */
    public static int between(int minimum, int maximum) {

        if (minimum > maximum) {
            throw new IllegalArgumentException("The minimum value must be less than the maximum");
        }
        return minimum + RANDOM.nextInt(maximum - minimum + 1);
    }
}
