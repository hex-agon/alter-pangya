package work.fking.pangya.login.packet.handler;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import work.fking.pangya.login.networking.ConnectionState;
import work.fking.pangya.login.packet.inbound.CheckNicknamePacket;
import work.fking.pangya.test.PacketHandlerTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing SetNicknamePacketHandler")
@TestInstance(Lifecycle.PER_CLASS)
public class SetNicknamePacketHandlerTest {

    private EmbeddedChannel channel;

    @BeforeEach
    public void beforeEach() {
        var handler = new SetNicknamePacketHandler();
        channel = new EmbeddedChannel(PacketHandlerTestUtils.channelHandlerFor(handler));
    }

    @AfterEach
    public void afterAll() {
        channel.disconnect();
    }

    @Test
    @DisplayName("Should disconnect if the state is invalid")
    public void test_invalid_state() {
        var packet = new CheckNicknamePacket("testnick");
        channel.writeInbound(packet);

        assertFalse(channel.isActive());
    }

    @Test
    @DisplayName("Should not disconnect and should return a response")
    public void test_valid_state() {
        channel.attr(ConnectionState.KEY).set(ConnectionState.SELECTING_NICKNAME);
        var packet = new CheckNicknamePacket("testnick");
        channel.writeInbound(packet);

        var outboundPacket = channel.readOutbound();

        assertTrue(channel.isActive());
        assertNotNull(outboundPacket);
    }
}
