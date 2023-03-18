package work.fking.pangya.login.net;

import work.fking.pangya.login.packet.handler.CheckNicknamePacketHandler;
import work.fking.pangya.login.packet.handler.GhostClientPacketHandler;
import work.fking.pangya.login.packet.handler.LoginPacketHandler;
import work.fking.pangya.login.packet.handler.ReconnectPacketHandler;
import work.fking.pangya.login.packet.handler.SelectCharacterPacketHandler;
import work.fking.pangya.login.packet.handler.SelectServerPacketHandler;
import work.fking.pangya.login.packet.handler.SetNicknamePacketHandler;

public enum ClientLoginPacketType {

    LOGIN_REQUEST(0x1, new LoginPacketHandler()),
    SELECT_SERVER(0x3, new SelectServerPacketHandler()),
    GHOST_CLIENT(0x4, new GhostClientPacketHandler()),
    SET_NICKNAME(0x6, new SetNicknamePacketHandler()),
    CHECK_NICKNAME(0x7, new CheckNicknamePacketHandler()),
    SELECT_CHARACTER(0x8, new SelectCharacterPacketHandler()),
    RECONNECT_REQUEST(0xb, new ReconnectPacketHandler())
    ;

    private final int id;
    private final ClientLoginPacketHandler handler;

    ClientLoginPacketType(int id) {
        this(id, (server, player, packet) -> {
        }); // discard packet
    }

    ClientLoginPacketType(int id, ClientLoginPacketHandler handler) {
        this.id = id;
        this.handler = handler;
    }

    public int id() {
        return id;
    }

    public ClientLoginPacketHandler handler() {
        return handler;
    }
}
