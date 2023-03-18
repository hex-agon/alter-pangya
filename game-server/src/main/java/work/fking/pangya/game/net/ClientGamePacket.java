package work.fking.pangya.game.net;

import io.netty.buffer.ByteBuf;

public record ClientGamePacket(ClientGamePacketType type, ByteBuf buffer) {

}
