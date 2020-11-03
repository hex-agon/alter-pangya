package work.fking.pangya.common.model;

import java.util.List;

public record GameServer(int id, int capacity, int playerCount, String ip, int port, List<GameServerFlag> flags, List<GameServerFlag> boosts, GameServerIcon icon) {

}
