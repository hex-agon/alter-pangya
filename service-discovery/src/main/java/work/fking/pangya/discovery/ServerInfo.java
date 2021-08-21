package work.fking.pangya.discovery;

import work.fking.pangya.common.model.server.ServerBoost;
import work.fking.pangya.common.model.server.ServerFlag;
import work.fking.pangya.common.model.server.ServerIcon;

import java.util.List;

public record ServerInfo(
        ServerType type,
        int id,
        String name,
        int capacity,
        int playerCount,
        String ip,
        int port,
        List<ServerFlag> flags,
        List<ServerBoost> boosts,
        ServerIcon icon
) {

}
