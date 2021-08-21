package work.fking.pangya.game.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.lettuce.core.RedisClient;
import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.discovery.DiscoveryClient;

import javax.inject.Singleton;

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
    public DiscoveryClient provideDiscoveryClient(RedisClient redisClient) {
        return DiscoveryClient.create(redisClient);
    }

    @Provides
    @Singleton
    public ServerConfig provideServerConfig() {
        return serverConfig;
    }
}
