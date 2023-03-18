package work.fking.pangya.discovery;

import work.fking.pangya.common.server.ServerBoost;
import work.fking.pangya.common.server.ServerFlag;
import work.fking.pangya.common.server.ServerIcon;

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
