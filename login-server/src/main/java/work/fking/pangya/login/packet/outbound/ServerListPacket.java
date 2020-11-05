package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class ServerListPacket implements OutboundPacket {

    private static final int ID = 2;

    @Override
    public void encode(ByteBuf target) {

        target.writeShortLE(ID);
        target.writeByte(1); // server list size

        /*
        flags
            0x10 hides the server
            0x80 = sort priority
            0x800 grand prix

        server icons
            0 black papel
            1 pippin
            2 titan boo
            3 dolfini
            4 lolo
            5 quma
            6 tiki
            7 cadie
            8 cien

        server boosts
            0x2 double pang
            0x4 double xp
            0x8 angel event (low quit rate players 0-1%)
            0x10 triple exp
            0x80 club mastery (clubset points)
        */

        // repeat for each
        ProtocolUtils.writeFixedSizeString(target, "Testing", 40);
        target.writeIntLE(20202); // serverId
        target.writeIntLE(1000); // capacity
        target.writeIntLE(500); // playerCount
        ProtocolUtils.writeFixedSizeString(target, "127.0.0.1", 18); // serverIp
        target.writeShortLE(20202); // serverPort
        target.writeShortLE(0); // unknown, used just to skip
        target.writeShortLE(0); // flags?  0x10 hides the server? 0x80 = sort priority, 0x800 grand prix
        target.writeIntLE(0xFFFFFFFF); // unknown, used just to skip
        target.writeShortLE(0xFFFF); // unknown, used just to skip
        target.writeByte(0x8); // server boosts
        target.writeIntLE(0xFFFFFFFF); // unknown, used just to skip
        target.writeShortLE(0xFFFF); // unknown, used just to skip
        target.writeByte(0xFF); // unknown, used just to skip
        target.writeShortLE(0); // serverIcon
    }
}
