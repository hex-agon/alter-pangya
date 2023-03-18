package work.fking.pangya.login.net;

import io.netty.buffer.ByteBuf;

public record ClientLoginPacket(ClientLoginPacketType type, ByteBuf buffer) {

}
