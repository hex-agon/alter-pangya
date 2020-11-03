package work.fking.pangya.common;

import lombok.extern.log4j.Log4j2;

import java.lang.Thread.UncaughtExceptionHandler;

@Log4j2
public class Log4jExceptionLogger implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.catching(e);
    }
}
