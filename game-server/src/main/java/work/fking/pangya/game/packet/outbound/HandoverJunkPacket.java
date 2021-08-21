package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class HandoverJunkPacket implements OutboundPacket {

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
        target.writeByte(0xD3); // see table above for possible values
        target.writeByte(0);

        // int[] values = {1};
        // // updates the progress bar, we don't need to send this at all
        // for (int value : values) {
        //     target.writeShortLE(0x44);
        //     target.writeByte(0xD2);
        //     target.writeIntLE(value);
        // }

        // unknown doesn't seem to be necessary
        // target.writeBytes(new byte[] {0x1F, 0x01, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});

        target.writeShortLE(0x44);
        target.writeByte(0);
        ProtocolUtils.writePString(target, "US852"); // server version
        ProtocolUtils.writePString(target, "hexserver_dev"); // server name
    }
}
