package work.fking.pangya.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

public class Log4jExceptionLogger implements UncaughtExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(Log4jExceptionLogger.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.catching(e);
    }
}
