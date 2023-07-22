package work.fking.pangya.login.net

import work.fking.pangya.login.packet.handler.CheckNicknamePacketHandler
import work.fking.pangya.login.packet.handler.ReconnectPacketHandler
import work.fking.pangya.login.packet.handler.SelectCharacterPacketHandler
import work.fking.pangya.login.packet.handler.SelectServerPacketHandler
import work.fking.pangya.login.packet.handler.SetNicknamePacketHandler

enum class ClientPacketType(
    private val id: Int,
    private val handler: ClientPacketHandler = ClientPacketHandler { _, _, _ -> }
) {
    SELECT_SERVER(0x3, SelectServerPacketHandler()),
    GHOST_CLIENT(0x4),
    SET_NICKNAME(0x6, SetNicknamePacketHandler()),
    CHECK_NICKNAME(0x7, CheckNicknamePacketHandler()),
    SELECT_CHARACTER(0x8, SelectCharacterPacketHandler());

    fun id(): Int {
        return id
    }

    fun handler(): ClientPacketHandler {
        return handler
    }
}
