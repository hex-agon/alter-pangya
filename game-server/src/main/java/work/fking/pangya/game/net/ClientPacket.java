package work.fking.pangya.game.net;

import io.netty.buffer.ByteBuf;

public record ClientPacket(ClientPacketType type, ByteBuf buffer) {

}
