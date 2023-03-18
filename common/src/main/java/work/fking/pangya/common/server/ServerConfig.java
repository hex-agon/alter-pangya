package work.fking.pangya.common.server;

import java.util.List;

public record ServerConfig(
        int id,
        String name,
        int capacity,
        String bindAddress,
        String advertiseAddress,
        int port,
        List<ServerFlag> flags,
        List<ServerBoost> boosts,
        ServerIcon icon
) {

}
