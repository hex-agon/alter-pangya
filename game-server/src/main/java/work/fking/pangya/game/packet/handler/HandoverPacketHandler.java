package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.game.packet.inbound.HandoverPacket;
import work.fking.pangya.game.packet.outbound.CharacterHosterPacket;
import work.fking.pangya.game.packet.outbound.CookieBalancePacket;
import work.fking.pangya.game.packet.outbound.EquipmentPacket;
import work.fking.pangya.game.packet.outbound.InventoryPacket;
import work.fking.pangya.game.packet.outbound.MascotRosterPacket;
import work.fking.pangya.game.packet.outbound.OopsiePacket;
import work.fking.pangya.game.packet.outbound.PangBalancePacket;
import work.fking.pangya.game.packet.outbound.ServerChannelsPacket;
import work.fking.pangya.game.packet.outbound.Unknown305Packet;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Singleton
public class HandoverPacketHandler implements InboundPacketHandler<HandoverPacket> {

    private static final Logger LOGGER = LogManager.getLogger(HandoverPacketHandler.class);

    @Override
    public void handle(Channel channel, HandoverPacket packet) {
        LOGGER.debug(packet);

        channel.write(new OopsiePacket());
        channel.write(new ServerChannelsPacket());
        channel.write(new CharacterHosterPacket());
        channel.write(new InventoryPacket());
        channel.write(new MascotRosterPacket());
        channel.write(new EquipmentPacket());
        channel.write(new CookieBalancePacket());
        channel.write(new PangBalancePacket());
        channel.write(new Unknown305Packet());
        channel.flush();
        channel.disconnect();
    }
}
