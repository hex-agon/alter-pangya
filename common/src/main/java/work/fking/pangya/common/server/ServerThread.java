package work.fking.pangya.common.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.common.Time;

import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;

public class ServerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerThread.class);

    private final int interval;
    private final Runnable tick;

    private volatile boolean running = true;
    private long nextTick = Time.monotonicMillis();

    private ServerThread(Runnable tick, int interval) {
        this.setName("ServerThread");
        this.tick = tick;
        this.interval = interval;
    }

    public static ServerThread create(Runnable tick) {
        return create(tick, 500);
    }

    public static ServerThread create(Runnable tick, int interval) {
        return new ServerThread(tick, interval);
    }

    public void shutdown() {
        running = false;
    }

    @Override
    public void run() {

        while (running) {
            nextTick += interval;
            try {
                tick.run();
                awaitNext(() -> Time.monotonicMillis() < nextTick);
            } catch (Throwable e) {
                LOGGER.warn("Exception caught in the server thread", e);
            }
        }
    }

    private void awaitNext(BooleanSupplier supplier) {
        while (supplier.getAsBoolean()) {
            Thread.yield();
            LockSupport.parkNanos("Sleeping until the next tick", 10_000_000L);
        }
    }
}
