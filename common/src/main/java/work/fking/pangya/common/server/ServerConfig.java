package work.fking.pangya.common.server;

import work.fking.pangya.common.model.server.ServerBoost;
import work.fking.pangya.common.model.server.ServerFlag;
import work.fking.pangya.common.model.server.ServerIcon;

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
