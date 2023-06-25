package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class CookieBalancePacket implements OutboundPacket {

    private static final int ID = 0x96;

    private final int balance;

    public CookieBalancePacket(int balance) {
        this.balance = balance;
    }

    public static CookieBalancePacket create(Player player) {
        return new CookieBalancePacket(player.cookieBalance());
    }

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeLongLE(balance);
    }
}
