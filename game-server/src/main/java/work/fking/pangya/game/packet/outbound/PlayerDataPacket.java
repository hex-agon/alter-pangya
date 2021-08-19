package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class PlayerDataPacket implements OutboundPacket {

    @Override
    public void encode(ByteBuf target) {

        // General info
        target.writeShortLE(0xFFFF);
        ProtocolUtils.writeFixedSizeString(target, "hexagon", 21); // username
        ProtocolUtils.writeFixedSizeString(target, "Hex agon", 21); // nickname
        target.writeBytes(new byte[0x20]);
        target.writeByte(0xF); // gm flag
        target.writeBytes(new byte[6]);
        target.writeIntLE(0); // connectionId
        target.writeBytes(new byte[0x1F]);
        target.writeByte(0); // chat restricted? 0xf yes
        target.writeBytes(new byte[0x8A]);

        // Statistics
        target.writeIntLE(0);
        target.writeIntLE(20); // totalStroke
        target.writeIntLE(350); // total playtime in seconds
        target.writeIntLE(5); // averageStrokeTime
        target.writeBytes(new byte[0xB]);
        target.writeIntLE(1); // OB Rate
        target.writeIntLE(1); // Total distance
        target.writeIntLE(342); // Total holes
        target.writeIntLE(2);
        target.writeIntLE(333); // Hio count
        target.writeBytes(new byte[0x19]);
        target.writeIntLE(53489); // Experience
        target.writeByte(0x23); // Rank
        target.writeLongLE(9102393); // pang earned
        target.writeBytes(new byte[0x39]);
        target.writeIntLE(1); // Quit rate 1
        target.writeBytes(new byte[0x1F]);
        target.writeIntLE(1); // Game combo part 1
        target.writeIntLE(1); // Game combo part 2
        target.writeIntLE(1); // Quit rate 2
        target.writeLongLE(467); // pang battle won
        target.writeBytes(new byte[0x25]);

        // Unknown
        target.writeBytes(new byte[0x4D]);

        // Equipment
        target.writeIntLE(0); // Caddie Id (iff id?)
        target.writeIntLE(0); // Character Id (iff id?)
        target.writeIntLE(0); // Club set Id (iff id?)
        target.writeIntLE(335544320); // Aztec iff id

        // Equipment - inventory items
        for (int i = 0; i < 9; i++) {
            target.writeIntLE(402653188); // item iff id
        }
        target.writeIntLE(0);
        target.writeIntLE(0);
        target.writeIntLE(0);
        target.writeIntLE(0);
        target.writeIntLE(0);
        target.writeIntLE(0);

        // Equipment - Decorations
        target.writeIntLE(0); // background (iff id?)
        target.writeIntLE(0); // frame (iff id?)
        target.writeIntLE(0); // sticker (iff id?)
        target.writeIntLE(0); // slot (iff id?)
        target.writeIntLE(0); // unknown (iff id?)
        target.writeIntLE(0); // title (iff id?)

        target.writeIntLE(268435466); // Mascot Id (iff id?)
        target.writeIntLE(0);
        target.writeIntLE(0);

        // Unknown 2
        target.writeBytes(new byte[0x2A53]);

        // Equipped Character
        target.writeIntLE(67108874); // iff id
        target.writeIntLE(1); // id??
        target.writeIntLE(1); // hair color
        target.writeBytes(new byte[0x1F7]);

        // Equipped Caddie
        target.writeIntLE(0); // slot
        target.writeIntLE(469762055); // iff id
        target.writeIntLE(0); // unknown, unconfirmed
        target.writeByte(0); // unknown, unconfirmed
        target.writeIntLE(0); // unknown, unconfirmed
        target.writeLongLE(0); // unknown, unconfirmed

        // Equipped club
        target.writeIntLE(0); // slot
        target.writeIntLE(268435466); // iff id
        target.writeBytes(new byte[0x9]);
        target.writeShortLE(0); // power upgrades
        target.writeShortLE(0); // control upgrades
        target.writeShortLE(0); // accuracy upgrades
        target.writeShortLE(0); // spin upgrades
        target.writeShortLE(0); // curve upgrades

        // Equipped mascot
        target.writeIntLE(0); // slot
        target.writeIntLE(0); // iff id
        target.writeBytes(new byte[0x4]);
        target.writeBytes(new byte[0xF]);
        target.writeBytes(new byte[0x20]);
    }
}
