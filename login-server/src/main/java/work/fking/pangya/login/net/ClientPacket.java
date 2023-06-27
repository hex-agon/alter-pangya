package work.fking.pangya.login.net;

import io.netty.buffer.ByteBuf;

public record ClientPacket(ClientPacketType type, ByteBuf buffer) {

}
