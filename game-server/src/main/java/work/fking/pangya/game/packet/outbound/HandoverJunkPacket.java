package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class HandoverJunkPacket implements OutboundPacket {

    private static final int ID = 150;

    @Override
    public void encode(ByteBuf target) {
        // we should call it the handoverresult packet
        // 0x44
        // 1, 9, 10, 12, 13 = causes the client to send select server packet & reconnect to the login server, probably used if the login key doesn't match
        // 3 = cannot connect to the login server
        // 5 = this id has been permanently blocked, contact support...
        // 7 = this id has been blocked, contact support...
        // 9 = reconnects too
        // 11 = Server version missmatch
        // 14, 15 = only certain allowed users may join this server.
        // 16, 17 = pangya is not available in your area
        // 19...31 = Account has been transferred
        // 210 = updates progress bar
        // 211 = login ok?

        //
        target.writeShortLE(0x44);
        target.writeByte(0xD3);
        target.writeByte(0);

        int[] values = {0x01, 0x03, 0x1C, 0x1E, 0x20, 0x05, 0x08, 0x0B, 0x10, 0x12};
        // progress bar
        for (int i = 0; i < values.length; i++) {
            target.writeShortLE(0x44);
            target.writeByte(0xD2);
            target.writeIntLE(values[i]);
        }

        //unknown
        target.writeBytes(new byte[] {0x1F, 0x01, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});

        int[] values2 = {0x15, 0x0E, 0x14, 0x16, 0x18, 0x1A, 0x22};
        // progress bar
        for (int i = 0; i < values2.length; i++) {
            target.writeShortLE(0x44);
            target.writeByte(0xD2);
            target.writeIntLE(values2[i]);
        }

        target.writeShortLE(0x44);
        target.writeByte(0);
        ProtocolUtils.writePString(target, "US852");
        ProtocolUtils.writePString(target, "hexserver_dev");
    }
}
