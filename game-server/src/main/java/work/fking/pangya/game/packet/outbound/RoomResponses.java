package work.fking.pangya.game.packet.outbound;

import work.fking.pangya.game.player.Character;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler.Course;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler.HoleMode;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler.RoomType;
import work.fking.pangya.networking.protocol.OutboundPacket;

import static work.fking.pangya.networking.protocol.ProtocolUtils.writeFixedSizeString;
import static work.fking.pangya.networking.protocol.ProtocolUtils.writePString;

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

            writeFixedSizeString(buffer, name, 64);
            buffer.writeZero(25);
            buffer.writeShortLE(id);
            buffer.writeZero(111);
            buffer.writeIntLE(0);
            buffer.writeIntLE(0);
        };
    }

    public static OutboundPacket leaveSuccess() {
        return buffer -> {
            buffer.writeShortLE(LEAVE_ID);
            buffer.writeShortLE(-1); // new room id (-1 = lobby)
        };
    }

    public static OutboundPacket roomInfo(RoomType roomType, String name, Course course, HoleMode holeMode, int holeCount, int maxPlayers, int shotTime, int gameTime) {
        return buffer -> {
            buffer.writeShortLE(SETTINGS_ID);
            buffer.writeShortLE(-1); // unknown constant -1
            buffer.writeByte(roomType.id()); // the room type
            buffer.writeByte(course.ordinal());
            buffer.writeByte(holeCount); // hole amount
            buffer.writeByte(holeMode.ordinal()); // hole progression, 0 = normal, 1 = backwards, 2 = random start, 3 = shuffle

            // if mode == REPEAT then write byte (likely a boolean) & int as the repeating role
            if (holeMode == HoleMode.REPEAT) {
                buffer.writeByte(1);
            }

            buffer.writeIntLE(0); // natural wind, 0 disabled, 1 = enabled
            buffer.writeByte(maxPlayers); // max players
            buffer.writeZero(2);
            buffer.writeIntLE(shotTime); // shot time
            buffer.writeIntLE(gameTime); // game time
            buffer.writeIntLE(0); // 00,00,00,00 in VS and Chat, 00,00,03,2C in Tournament and Battle, probably the trophy
            buffer.writeByte(1); // is room owner
            writePString(buffer, name);
        };
    }

    public static OutboundPacket roomInitialCensus() {
        return buffer -> {
            buffer.writeShortLE(CENSUS_ID);
            buffer.writeByte(0); // initial room census

            buffer.writeShortLE(-1);

            buffer.writeByte(1); // player count

            // user info
            var mock = Character.mock();

            buffer.writeIntLE(10); // connectionId
            writeFixedSizeString(buffer, "Beep", 22);
            writeFixedSizeString(buffer, "", 17); // guildName
            buffer.writeByte(2); // room slot, starting at 1
            buffer.writeIntLE(0); // unknown
            buffer.writeIntLE(0); // title
            buffer.writeIntLE(mock.iffId()); // character iff
            buffer.writeIntLE(0); // skin id background
            buffer.writeIntLE(0); // skin id frame
            buffer.writeIntLE(0); // skin id sticker
            buffer.writeIntLE(0); // skin id slot
            buffer.writeIntLE(0); // unknown
            buffer.writeIntLE(0); // duplicate skin id title

            buffer.writeShortLE(8); // unknown

            buffer.writeByte(10); // possibly the user rank
            buffer.writeZero(3);
            buffer.writeByte(0);
            buffer.writeShortLE(0);
            buffer.writeIntLE(0); // guild id
            writeFixedSizeString(buffer, "", 12);
            buffer.writeByte(0); // guild emblem id
            buffer.writeIntLE(1335); // player id?
            buffer.writeIntLE(0);
            buffer.writeShortLE(0);
            buffer.writeIntLE(0);

            // location for lounges
            buffer.writeFloatLE(0); // unknown
            buffer.writeFloatLE(0); // unknown
            buffer.writeFloatLE(0); // unknown
            buffer.writeFloatLE(0); // unknown

            // lounge shop
            buffer.writeIntLE(0); // shop active?
            writeFixedSizeString(buffer, "", 64); // shop name

            buffer.writeIntLE(0); // mascot id
            writeFixedSizeString(buffer, "", 22);
            buffer.writeZero(106);

            buffer.writeByte(0); // invited?

            buffer.writeFloatLE(0); // avg score

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
