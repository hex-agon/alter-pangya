package work.fking.pangya.common

object Time {
    fun monotonicMillis(): Long {
        return monotonicNanos() / 1000000L
    }

    fun monotonicNanos(): Long {
        return System.nanoTime()
    }
}
