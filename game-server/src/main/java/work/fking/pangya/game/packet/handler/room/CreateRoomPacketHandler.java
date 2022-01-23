package work.fking.pangya.game.packet.handler.room;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.game.packet.inbound.room.CreateRoomPacket;
import work.fking.pangya.game.packet.outbound.RoomResponses;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class CreateRoomPacketHandler implements InboundPacketHandler<CreateRoomPacket> {

    private static final Logger LOGGER = LogManager.getLogger(CreateRoomPacketHandler.class);

    @Override
    public void handle(Channel channel, CreateRoomPacket packet) {
        LOGGER.debug(packet);
        channel.write(RoomResponses.createSuccess(packet.name(), 22));
//        channel.write(RoomResponses.settings());
        channel.flush();
//        channel.writeAndFlush(RoomResponses.roomInitialCensus());
    }
}
