package work.fking.pangya.game.net;

import work.fking.pangya.game.packet.handler.EquipmentUpdatePacketHandler;
import work.fking.pangya.game.packet.handler.LeaveLobbyPacketHandler;
import work.fking.pangya.game.packet.handler.LockerInventoryRequestPacketHandler;
import work.fking.pangya.game.packet.handler.LoginBonusClaimPacketHandler;
import work.fking.pangya.game.packet.handler.LoginBonusStatusPacketHandler;
import work.fking.pangya.game.packet.handler.MyRoomOpenPacketHandler;
import work.fking.pangya.game.packet.handler.MyRoomOpenedPacketHandler;
import work.fking.pangya.game.packet.handler.PapelShopPlayPacketHandler;
import work.fking.pangya.game.packet.handler.RareShopOpenPacketHandler;
import work.fking.pangya.game.packet.handler.SelectChannelPacketHandler;
import work.fking.pangya.game.packet.handler.UpdateChatMacrosPacketHandler;
import work.fking.pangya.game.packet.handler.UserProfileRequestPacketHandler;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler;
import work.fking.pangya.game.packet.handler.room.LeaveRoomPacketHandler;

public enum ClientPacketType {

    SELECT_CHANNEL(0x4, new SelectChannelPacketHandler()),
    ROOM_CREATE(0x8, new CreateRoomPacketHandler()),
    ROOM_LEAVE(0xf, new LeaveRoomPacketHandler()),
    EQUIPMENT_UPDATE(0x20, new EquipmentUpdatePacketHandler()),
    USER_PROFILE_REQUEST(0x2f, new UserProfileRequestPacketHandler()),
    LOUNGE_USER_ACTION(0x63),
    UPDATE_MACROS(0x69, new UpdateChatMacrosPacketHandler()),
    LEAVE_LOBBY(0x82, new LeaveLobbyPacketHandler()),
    RARE_SHOP_OPEN(0x98, new RareShopOpenPacketHandler()),
    MY_ROOM_OPEN(0xb5, new MyRoomOpenPacketHandler()),
    MY_ROOM_OPENED(0xb7, new MyRoomOpenedPacketHandler()),
    UNKNOWN_9C(0x9c),
    UNKNOWN_C1(0xC1),
    LOCKER_INVENTORY_REQUEST(0xd3, new LockerInventoryRequestPacketHandler()),
    UNKNOWN_140(0x140),
    ACHIEVEMENT_STATUS_REQUEST(0x157),
    PAPEL_SHOP_PLAY(0x14b, new PapelShopPlayPacketHandler()),
    LOGIN_BONUS_INFO(0x16e, new LoginBonusStatusPacketHandler()),
    LOGIN_BONUS_CLAIM(0x16f, new LoginBonusClaimPacketHandler());

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
