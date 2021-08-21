package work.fking.pangya.discovery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.reactive.ChannelMessage;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiscoveryClient {

    private static final Logger LOGGER = LogManager.getLogger(DiscoveryClient.class);

    private static final String CHANNEL_NAME = "pangya.servers.heartbeat";
    private static final int KNOWN_SERVER_HEARTBEAT_AGE_MINUTES = 3;

    private final List<KnownServer> knownServers = new ArrayList<>();
    private final RedisPubSubCommands<String, String> redis;

    private final ObjectReader serverInfoReader;
    private final ObjectWriter serverInfoWriter;

    private DiscoveryClient(RedisPubSubCommands<String, String> redis, ObjectReader serverInfoReader, ObjectWriter serverInfoWriter) {
        this.redis = redis;
        this.serverInfoReader = serverInfoReader;
        this.serverInfoWriter = serverInfoWriter;
    }

    public static DiscoveryClient create(RedisURI redisURI) {
        return create(RedisClient.create(redisURI));
    }

    public static DiscoveryClient create(RedisClient redisClient) {
        var subConnection = redisClient.connectPubSub();

        var reactive = subConnection.reactive();
        reactive.subscribe(CHANNEL_NAME).subscribe();

        var objectMapper = new ObjectMapper();
        var serverInfoReader = objectMapper.readerFor(ServerInfo.class);
        var serverInfoWriter = objectMapper.writerFor(ServerInfo.class);

        var pubConnection = redisClient.connectPubSub();
        var discoveryClient = new DiscoveryClient(pubConnection.sync(), serverInfoReader, serverInfoWriter);

        reactive.observeChannels()
                .filter(channel -> channel.getChannel().equals(CHANNEL_NAME))
                .doOnNext(discoveryClient::onChannelMessage)
                .subscribe();

        return discoveryClient;
    }

    /**
     * Retrieves all the instances of the requested server type.
     *
     * @return A list containing all the found servers.
     */
    public List<ServerInfo> instances(ServerType serverType) {
        synchronized (knownServers) {
            cleanup();
            return knownServers.stream()
                               .map(KnownServer::info)
                               .filter(info -> info.type() == serverType)
                               .toList();
        }
    }

    /**
     * Synchronously publishes the server info to be discovered by other servers.
     *
     * @param serverInfo The server info to be published.
     */
    public void publish(ServerInfo serverInfo) {
        try {
            var serializedServerInfo = serverInfoWriter.writeValueAsString(serverInfo);
            redis.publish(CHANNEL_NAME, serializedServerInfo);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Failed to encode ServerInfo", e);
        }
    }

    private void onChannelMessage(ChannelMessage<String, String> message) {
        try {
            ServerInfo serverInfo = serverInfoReader.readValue(message.getMessage());
            register(serverInfo);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Failed to decode ServerInfo message", e);
        }
    }

    private void register(ServerInfo info) {
        var knownServer = new KnownServer(LocalDateTime.now(), info);
        synchronized (knownServers) {
            var idx = findKnownServerIndex(info.id());

            if (idx == -1) {
                LOGGER.trace("New server discovered, {} @ {}:{}", info.name(), info.ip(), info.port());
                knownServers.add(knownServer);
            } else {
                LOGGER.trace("Updating known server, {} @ {}:{}", info.name(), info.ip(), info.port());
                knownServers.set(idx, knownServer);
            }
        }
    }

    private int findKnownServerIndex(int serverId) {
        for (int i = 0; i < knownServers.size(); i++) {
            if (knownServers.get(i).info().id() == serverId) {
                return i;
            }
        }
        return -1;
    }

    private void cleanup() {
        var now = LocalDateTime.now();

        synchronized (knownServers) {
            var iterator = knownServers.iterator();

            while (iterator.hasNext()) {
                KnownServer server = iterator.next();

                if (Duration.between(server.knownSince(), now).toMinutes() > KNOWN_SERVER_HEARTBEAT_AGE_MINUTES) {
                    var info = server.info();
                    LOGGER.debug("No new heartbeats received from server {} @ {}:{}, removing it.", info.name(), info.ip(), info.port());
                    iterator.remove();
                }
            }
        }
    }

    private static record KnownServer(
            LocalDateTime knownSince,
            ServerInfo info
    ) {

    }
}
