package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.UpdateChatMacrosPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class UpdateChatMacrosPacketHandler implements InboundPacketHandler<UpdateChatMacrosPacket> {

    @Override
    public void handle(Channel channel, UpdateChatMacrosPacket packet) {
        System.out.println(packet);
    }
}
