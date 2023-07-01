package work.fking.pangya.game.net

import work.fking.pangya.game.packet.handler.EquipmentUpdatePacketHandler
import work.fking.pangya.game.packet.handler.LeaveLobbyPacketHandler
import work.fking.pangya.game.packet.handler.LockerInventoryRequestPacketHandler
import work.fking.pangya.game.packet.handler.LoginBonusClaimPacketHandler
import work.fking.pangya.game.packet.handler.LoginBonusStatusPacketHandler
import work.fking.pangya.game.packet.handler.MyRoomOpenPacketHandler
import work.fking.pangya.game.packet.handler.MyRoomOpenedPacketHandler
import work.fking.pangya.game.packet.handler.PapelShopPlayPacketHandler
import work.fking.pangya.game.packet.handler.RareShopOpenPacketHandler
import work.fking.pangya.game.packet.handler.SelectChannelPacketHandler
import work.fking.pangya.game.packet.handler.UpdateChatMacrosPacketHandler
import work.fking.pangya.game.packet.handler.UserProfileRequestPacketHandler
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler
import work.fking.pangya.game.packet.handler.room.LeaveRoomPacketHandler
import work.fking.pangya.game.packet.handler.room.RoomSettingsUpdatePacketHandler

enum class ClientPacketType(
    private val id: Int,
    private val handler: ClientPacketHandler = ClientPacketHandler { _, _, _ -> }
) {
    SELECT_CHANNEL(0x4, SelectChannelPacketHandler()),
    ROOM_CREATE(0x8, CreateRoomPacketHandler()),
    ROOM_SETTINGS_UPDATE(0xa, RoomSettingsUpdatePacketHandler()),
    ROOM_LEAVE(0xf, LeaveRoomPacketHandler()),
    EQUIPMENT_UPDATE(0x20, EquipmentUpdatePacketHandler()),
    USER_PROFILE_REQUEST(0x2f, UserProfileRequestPacketHandler()),
    LOUNGE_USER_ACTION(0x63),
    UPDATE_MACROS(0x69, UpdateChatMacrosPacketHandler()),
    LEAVE_LOBBY(0x82, LeaveLobbyPacketHandler()),
    RARE_SHOP_OPEN(0x98, RareShopOpenPacketHandler()),
    MY_ROOM_OPEN(0xb5, MyRoomOpenPacketHandler()),
    MY_ROOM_OPENED(0xb7, MyRoomOpenedPacketHandler()),
    UNKNOWN_9C(0x9c),
    UNKNOWN_C1(0xC1),
    LOCKER_INVENTORY_REQUEST(0xd3, LockerInventoryRequestPacketHandler()),
    UNKNOWN_140(0x140),
    ACHIEVEMENT_STATUS_REQUEST(0x157),
    PAPEL_SHOP_PLAY(0x14b, PapelShopPlayPacketHandler()),
    LOGIN_BONUS_INFO(0x16e, LoginBonusStatusPacketHandler()),
    LOGIN_BONUS_CLAIM(0x16f, LoginBonusClaimPacketHandler());

    fun id(): Int {
        return id
    }

    fun handler(): ClientPacketHandler {
        return handler
    }
}
