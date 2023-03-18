package work.fking.pangya.game.packet.outbound;

import work.fking.pangya.game.model.PangCharacter;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler.Course;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler.RoomType;
import work.fking.pangya.game.packet.inbound.room.CreateRoomPacket.HoleMode;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public final class RoomResponses {

    private static final int RESPONSE_ID = 0x49;
    private static final int SETTINGS_ID = 0x4a;
    private static final int LEAVE_ID = 0x4c;
    private static final int CENSUS_ID = 0x48;

    private static final int SUCCESS = 0;
    private static final int ALREADY_STARTED = 8;
    private static final int CANNOT_CREATE = 18;

    private RoomResponses() {
    }

    public static OutboundPacket createSuccess(String name, RoomType type, int id) {
        return buffer -> {
            buffer.writeShortLE(RESPONSE_ID);
            buffer.writeShortLE(SUCCESS);

            // https://github.com/Acrisio-Filho/SuperSS-Dev/blob/747ac1615c849ed13526415105edd0b4ebd7bc6c/Server%20Lib/Game%20Server/TYPE/pangya_game_st.h#L2417
            // https://github.com/luismk/UGPangya/blob/88495ab8614e8f7ed9eba224af875ad50a2ea9da/PangyaAPI/Repository/Models/Game.cs#L170
            ProtocolUtils.writeFixedSizeString(buffer, name, 64);
            buffer.writeBoolean(true); // password protected
            buffer.writeBoolean(true); // not started
            buffer.writeBoolean(false); // joinable after start
            buffer.writeByte(1); // max players
            buffer.writeByte(1); // player count

            buffer.writeZero(17); // room key
            buffer.writeByte(30); // unknown 30 constant
            buffer.writeByte(18); // holes
            buffer.writeByte(type.id()); // visible room type
            buffer.writeShortLE(10); // room number/id
            buffer.writeByte(HoleMode.BACK.ordinal());
            buffer.writeByte(Course.BLUE_LAGOON.ordinal());
            buffer.writeIntLE(30); // time vs
            buffer.writeIntLE(30); // time 30
            buffer.writeIntLE(0); // trophy
            buffer.writeShortLE(0); // gm event room (100 players)
            buffer.writeZero(66); // guild related
            buffer.writeIntLE(0); // pang rate
            buffer.writeIntLE(0); // exp rate
            buffer.writeIntLE(1335); // room owner id
            buffer.writeByte(0);
            buffer.writeIntLE(0); // artifact iff id
            buffer.writeIntLE(0); // natural mode
            buffer.writeIntLE(0); // grand prix related
            buffer.writeIntLE(0); // grand prix related
            buffer.writeIntLE(0); // grand prix time
            buffer.writeIntLE(0); // grand prix room?
        };
    }

    public static OutboundPacket leaveSuccess() {
        return buffer -> {
            buffer.writeShortLE(LEAVE_ID);
            buffer.writeShortLE(-1); // new room id (-1 = lobby)
        };
    }

    public static OutboundPacket info() {
        return buffer -> {
            buffer.writeShortLE(SETTINGS_ID);
            buffer.writeShortLE(-1); // unknown constant -1
            buffer.writeByte(RoomType.PRACTICE.id()); // the room type
            buffer.writeByte(Course.BLUE_LAGOON.ordinal());
            buffer.writeByte(18); // hole amount
            buffer.writeByte(0); // hole progression, 0 = normal, 1 = backwards, 2 = random start, 3 = shuffle

            // if mode == REPEAT then write byte (likely a boolean) & int as the repeating role

            buffer.writeIntLE(0); // natural wind, 0 disabled, 1 = enabled
            buffer.writeByte(1); // max players
            buffer.writeByte(30); // constant
            buffer.writeByte(1); // state flag? acrisio
            buffer.writeIntLE(30); // shot time
            buffer.writeIntLE(1800000); // game time
            buffer.writeIntLE(0); // 00,00,00,00 in VS and Chat, 00,00,03,2C in Tournament and Battle, probably the trophy
            buffer.writeByte(1);
            ProtocolUtils.writePString(buffer, "Single Player Practice Mode");
        };
    }

    public static OutboundPacket roomInitialCensus() {
        return buffer -> {
            buffer.writeShortLE(CENSUS_ID);
            buffer.writeByte(0); // initial room census

            buffer.writeByte(1); // player count

            // user info
            var mock = PangCharacter.mock();

            buffer.writeIntLE(0);
            ProtocolUtils.writeFixedSizeString(buffer, "Nickname", 22);
            ProtocolUtils.writeFixedSizeString(buffer, "", 21); // guildName
            buffer.writeByte(1); // room slot, starting at 1
            buffer.writeIntLE(0); // unknown
            buffer.writeIntLE(0); // title
            buffer.writeIntLE(mock.iffId()); // character iff
            buffer.writeIntLE(0); // skin id background
            buffer.writeIntLE(0); // skin id frame
            buffer.writeIntLE(0); // skin id sticker
            buffer.writeIntLE(0); // skin id slot
            buffer.writeIntLE(0); // unknown
            buffer.writeIntLE(0); // duplicate skin id title

            buffer.writeShortLE(1); // unknown

            buffer.writeByte(0x01); // possibly the user rank
            buffer.writeByte(0); // either 0 or 0x28 (observed)
            buffer.writeByte(0x0A); // unknown constant
            buffer.writeIntLE(0); // guild id
            ProtocolUtils.writeFixedSizeString(buffer, "guildmark", 12);
            buffer.writeIntLE(1); // user id
            buffer.writeIntLE(0); // unknown
            buffer.writeShortLE(3490); // unknown
            buffer.writeIntLE(0); // unknown either 0 or 1

            // location for lounges
            buffer.writeFloatLE(0); // unknown
            buffer.writeFloatLE(0); // unknown
            buffer.writeFloatLE(0); // unknown

            // lounge shop
            buffer.writeIntLE(0); // shop active?
            ProtocolUtils.writeFixedSizeString(buffer, "", 64); // shop name

            buffer.writeIntLE(0); // mascot id
            buffer.writeShortLE(0); // item boosts?

            buffer.writeIntLE(0);

            ProtocolUtils.writeFixedSizeString(buffer, "", 22); // user nt
            ProtocolUtils.writeFixedSizeString(buffer, "", 105); // user nt

            buffer.writeByte(0); // invited?

            buffer.writeFloatLE(0); // avg score
            buffer.writeFloatLE(0x42); // unknown

            // user character
            mock.encode(buffer);
        };
    }

    public static OutboundPacket loungePkt196() {
        return buffer -> {
            buffer.writeShortLE(0x196);
            buffer.writeIntLE(1);
            buffer.writeFloatLE(1);
            buffer.writeFloatLE(1);
            buffer.writeFloatLE(1);
            buffer.writeFloatLE(1);
        };
    }

    public static OutboundPacket loungePkt9e() {
        return buffer -> {
            buffer.writeShortLE(0x9e);
            buffer.writeShortLE(0); // weather
            buffer.writeIntLE(1);
        };
    }

    public static OutboundPacket alreadyStarted() {
        return buffer -> {
            buffer.writeShortLE(RESPONSE_ID);
            buffer.writeByte(ALREADY_STARTED);
        };
    }

    public static OutboundPacket cannotCreate() {
        return buffer -> {
            buffer.writeShortLE(RESPONSE_ID);
            buffer.writeByte(CANNOT_CREATE);
        };
    }
}
