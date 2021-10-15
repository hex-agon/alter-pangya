package work.fking.pangya.common;

public final class Time {

    private Time() {
    }

    public static long monotonicMillis() {
        return monotonicNanos() / 1000000L;
    }

    public static long monotonicNanos() {
        return System.nanoTime();
    }
}
