package work.fking.pangya.common.server

import org.slf4j.LoggerFactory
import work.fking.pangya.common.Time
import java.util.concurrent.locks.LockSupport
import java.util.function.BooleanSupplier

class ServerThread private constructor(tick: Runnable, interval: Int) : Thread() {
    private val interval: Int
    private val tick: Runnable

    @Volatile
    private var running = true
    private var nextTick = Time.monotonicMillis()

    init {
        name = "ServerThread"
        this.tick = tick
        this.interval = interval
    }

    fun shutdown() {
        running = false
    }

    override fun run() {
        while (running) {
            nextTick += interval.toLong()
            try {
                tick.run()
                awaitNext { Time.monotonicMillis() < nextTick }
            } catch (e: Throwable) {
                LOGGER.warn("Exception caught in the server thread", e)
            }
        }
    }

    private fun awaitNext(supplier: BooleanSupplier) {
        while (supplier.asBoolean) {
            yield()
            LockSupport.parkNanos("Sleeping until the next tick", 10000000L)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ServerThread::class.java)
        @JvmOverloads
        fun create(tick: Runnable, interval: Int = 500): ServerThread {
            return ServerThread(tick, interval)
        }
    }
}
