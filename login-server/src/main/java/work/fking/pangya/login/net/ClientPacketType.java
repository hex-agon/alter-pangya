package work.fking.pangya.login.net;

import work.fking.pangya.login.packet.handler.CheckNicknamePacketHandler;
import work.fking.pangya.login.packet.handler.ReconnectPacketHandler;
import work.fking.pangya.login.packet.handler.SelectCharacterPacketHandler;
import work.fking.pangya.login.packet.handler.SelectServerPacketHandler;
import work.fking.pangya.login.packet.handler.SetNicknamePacketHandler;

public enum ClientPacketType {

    SELECT_SERVER(0x3, new SelectServerPacketHandler()),
    GHOST_CLIENT(0x4),
    SET_NICKNAME(0x6, new SetNicknamePacketHandler()),
    CHECK_NICKNAME(0x7, new CheckNicknamePacketHandler()),
    SELECT_CHARACTER(0x8, new SelectCharacterPacketHandler()),
    RECONNECT_REQUEST(0xb, new ReconnectPacketHandler());

    private final int id;
    private final ClientPacketHandler handler;

    ClientPacketType(int id) {
        this(id, (server, player, packet) -> {
        }); // discard packet
    }

    ClientPacketType(int id, ClientPacketHandler handler) {
        this.id = id;
        this.handler = handler;
    }

    public int id() {
        return id;
    }

    public ClientPacketHandler handler() {
        return handler;
    }
}
