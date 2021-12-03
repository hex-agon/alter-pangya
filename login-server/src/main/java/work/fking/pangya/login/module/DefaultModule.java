package work.fking.pangya.login.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.lettuce.core.RedisClient;
import work.fking.pangya.common.Log4jExceptionLogger;
import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.discovery.DiscoveryClient;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class DefaultModule extends AbstractModule {

    private final ServerConfig serverConfig;

    private DefaultModule(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public static DefaultModule create(ServerConfig serverConfig) {
        return new DefaultModule(serverConfig);
    }

    @Provides
    @Singleton
    public ServerConfig provideServerConfig() {
        return serverConfig;
    }

    @Provides
    @Singleton
    @Named("shared")
    public ExecutorService provideSharedExecutorService() {
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory, new Log4jExceptionLogger(), true);
    }

    @Provides
    @Singleton
    public DiscoveryClient provideDiscoveryClient(RedisClient redisClient) {
        return DiscoveryClient.create(redisClient);
    }
}
