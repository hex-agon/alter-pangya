package work.fking.pangya.game;

import work.fking.pangya.game.player.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class ServerChannel {

    private final AtomicInteger playerCount = new AtomicInteger();
    private final Map<Integer, Player> players = new ConcurrentHashMap<>();

    private final int id;
    private final String name;
    private final int capacity;
    private final List<Restriction> restrictions;

    public ServerChannel(int id, String name, int capacity, List<Restriction> restrictions) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.restrictions = restrictions;
    }

    public Player findPlayer(int connectionId) {
        return players.get(connectionId);
    }

    void addPlayer(Player player) {
        players.put(player.connectionId(), player);
        playerCount.incrementAndGet();
    }

    void removePlayer(Player player) {
        players.remove(player.connectionId());
        playerCount.decrementAndGet();
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int capacity() {
        return capacity;
    }

    public List<Restriction> restrictions() {
        return restrictions;
    }

    public int playerCount() {
        return playerCount.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (ServerChannel) obj;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, capacity, restrictions);
    }

    @Override
    public String toString() {
        return "ServerChannel[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "capacity=" + capacity + ", " +
                "restrictions=" + restrictions + ']';
    }

    public enum Restriction {
        JUNIOR_AND_LOWER(1 << 1),
        ROOKIES_ONLY(1 << 3),
        BEGINNERS_AND_JUNIORS_ONLY(1 << 4),
        JUNIORS_AND_SENIORS_ONLY(1 << 5);
        private final int flag;

        Restriction(int flag) {
            this.flag = flag;
        }

        public int flag() {
            return flag;
        }
    }
}
