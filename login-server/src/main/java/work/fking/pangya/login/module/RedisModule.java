package work.fking.pangya.login.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class RedisModule extends AbstractModule {

    private RedisModule() {
    }

    @Override
    protected void configure() {
        Map<String, String> properties = new HashMap<>();

        properties.put("redisUri", System.getenv("REDIS_URI"));

        Names.bindProperties(binder(), properties);
    }

    public static RedisModule create() {
        return new RedisModule();
    }

    @Provides
    @Singleton
    public RedisClient provideRedisClient(@Named("redisUri") String redisUri) {
        return RedisClient.create(RedisURI.create(redisUri));
    }
}
