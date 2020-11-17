package work.fking.pangya.login.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import work.fking.pangya.common.Log4jExceptionLogger;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class SharedModule extends AbstractModule {

    @Provides
    @Singleton
    @Named("shared")
    public ExecutorService provideSharedExecutorService() {
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory, new Log4jExceptionLogger(), true);
    }

    @Provides
    @Singleton
    @Named("shared")
    public ObjectMapper provideSharedObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }
}
