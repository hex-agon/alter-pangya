package work.fking.pangya.discovery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.server.ServerBoost;
import work.fking.pangya.common.server.ServerFlag;
import work.fking.pangya.common.server.ServerIcon;
import work.fking.pangya.common.server.ServerConfig;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.IntSupplier;

public class HeartbeatPublisher {

    private static final Logger LOGGER = LogManager.getLogger(HeartbeatPublisher.class);
    private static final int PUBLISH_INTERVAL_MINUTES = 1;

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(
            runnable -> {
                var thread = new Thread(runnable);
                thread.setDaemon(true);
                thread.setName("HeartbeatPublisher");
                return thread;
            }
    );

    private final DiscoveryClient discoveryClient;
    private final StaticInfo staticInfo;
    private final IntSupplier playerCountSupplier;

    private HeartbeatPublisher(DiscoveryClient discoveryClient, StaticInfo staticInfo, IntSupplier playerCountSupplier) {
        this.discoveryClient = discoveryClient;
        this.staticInfo = staticInfo;
        this.playerCountSupplier = playerCountSupplier;
    }

    public static HeartbeatPublisher create(DiscoveryClient discoveryClient, ServerType serverType, ServerConfig serverConfig, IntSupplier playerCountSupplier) {
        var staticInfo = new StaticInfo(
                serverType,
                serverConfig.id(),
                serverConfig.name(),
                serverConfig.capacity(),
                serverConfig.advertiseAddress(),
                serverConfig.port(),
                serverConfig.flags(),
                serverConfig.boosts(),
                serverConfig.icon()
        );
        return new HeartbeatPublisher(discoveryClient, staticInfo, playerCountSupplier);
    }

    public void start() {
        EXECUTOR_SERVICE.scheduleWithFixedDelay(this::publish, 0, PUBLISH_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    private void publish() {
        try {
            var serverInfo = new ServerInfo(
                    staticInfo.type(),
                    staticInfo.id(),
                    staticInfo.name(),
                    staticInfo.capacity(),
                    playerCountSupplier.getAsInt(),
                    staticInfo.ip(),
                    staticInfo.port(),
                    staticInfo.flags(),
                    staticInfo.boosts(),
                    staticInfo.icon()
            );
            discoveryClient.publish(serverInfo);
        } catch (Exception e) {
            LOGGER.warn("Failed to publish heartbeat", e);
        }
    }

    private record StaticInfo(
            ServerType type,
            int id,
            String name,
            int capacity,
            String ip,
            int port,
            List<ServerFlag> flags,
            List<ServerBoost> boosts,
            ServerIcon icon
    ) {

    }
}
