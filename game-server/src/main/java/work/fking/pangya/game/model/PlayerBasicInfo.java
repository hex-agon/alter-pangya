package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;

import static work.fking.pangya.networking.protocol.ProtocolUtils.writeFixedSizeString;

public class PlayerBasicInfo {

    public static PlayerBasicInfo mock() {
        return new PlayerBasicInfo();
    }

    public void encode(ByteBuf buffer) {
        buffer.writeShortLE(0xFFFF); // roomId
        writeFixedSizeString(buffer, "hexagon", 22); // username
        writeFixedSizeString(buffer, "Hex agon", 22); // nickname
        writeFixedSizeString(buffer, "guildname", 17);
        writeFixedSizeString(buffer, "guildimg", 24);
        buffer.writeIntLE(10); // connection id?
        buffer.writeZero(12);
        buffer.writeIntLE(0);
        buffer.writeIntLE(0);
        buffer.writeShortLE(0);
        buffer.writeZero(6);
        buffer.writeZero(16);
        writeFixedSizeString(buffer, "guildimg", 128);
        buffer.writeIntLE(1335); // player id
    }
}
