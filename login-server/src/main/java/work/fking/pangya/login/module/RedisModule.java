package work.fking.pangya.login.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;

import javax.inject.Singleton;

public class RedisModule extends AbstractModule {

    public static RedisModule create() {
        return new RedisModule();
    }

    @Provides
    @Singleton
    public RedisClient provideRedisClient() {
        return RedisClient.create(RedisURI.create("redis://cubi.link"));
    }
}
