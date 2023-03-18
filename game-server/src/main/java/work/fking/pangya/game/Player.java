package work.fking.pangya.game;

import io.netty.channel.Channel;

public class Player {

    private final int id;
    private final Channel channel;

    public Player(int id, Channel channel) {
        this.id = id;
        this.channel = channel;
    }

    public int id() {
        return id;
    }

    public Channel channel() {
        return channel;
    }
}
