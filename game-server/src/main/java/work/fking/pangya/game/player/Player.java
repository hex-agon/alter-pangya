package work.fking.pangya.game.player;

import io.netty.channel.Channel;

public class Player {

    private final int uid;
    private final int connectionId;
    private final Channel channel;

    private final Inventory inventory = new Inventory();
    private final Equipment equipment = new Equipment(this);
    private final CharacterRoster characterRoster = new CharacterRoster();
    private final CaddieRoster caddieRoster = new CaddieRoster();

    private int pangBalance = 10000;
    private int cookieBalance;

    public Player(int uid, int connectionId, Channel channel) {
        this.uid = uid;
        this.connectionId = connectionId;
        this.channel = channel;
    }

    public int uid() {
        return uid;
    }

    public int connectionId() {
        return connectionId;
    }

    public Channel channel() {
        return channel;
    }

    public Inventory inventory() {
        return inventory;
    }

    public Equipment equipment() {
        return equipment;
    }

    public CharacterRoster characterRoster() {
        return characterRoster;
    }

    public CaddieRoster caddieRoster() {
        return caddieRoster;
    }

    public int pangBalance() {
        return pangBalance;
    }

    public void updatePangBalance(int delta) {
        pangBalance += delta;
    }

    public int cookieBalance() {
        return cookieBalance;
    }

    public void updateCookieBalance(int delta) {
        cookieBalance += delta;
    }

    public Character activeCharacter() {
        return Character.mock();
    }

    public Caddie activeCaddie() {
        return Caddie.mock();
    }
}
