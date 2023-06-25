package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class PangBalancePacket implements OutboundPacket {

    private static final int ID = 0xc8;

    private final int balance;

    public PangBalancePacket(int balance) {
        this.balance = balance;
    }

    public static PangBalancePacket create(Player player) {
        return new PangBalancePacket(player.pangBalance());
    }

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeLongLE(balance);
    }
}
