package work.fking.pangya.login.net

import work.fking.pangya.login.packet.handler.CheckNicknamePacketHandler
import work.fking.pangya.login.packet.handler.ConfirmNicknamePacketHandler
import work.fking.pangya.login.packet.handler.SelectCharacterPacketHandler
import work.fking.pangya.login.packet.handler.SelectServerPacketHandler

enum class ClientPacketType(
    val id: Int,
    val handler: ClientPacketHandler = ClientPacketHandler { _, _, _ -> }
) {
    SELECT_SERVER(0x3, SelectServerPacketHandler()),
    GHOST_CLIENT(0x4),
    CONFIRM_NICKNAME(0x6, ConfirmNicknamePacketHandler()),
    CHECK_NICKNAME(0x7, CheckNicknamePacketHandler()),
    SELECT_CHARACTER(0x8, SelectCharacterPacketHandler());
}
