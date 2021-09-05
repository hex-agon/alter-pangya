package work.fking.pangya.login.packet.handler;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.discovery.ServerType;
import work.fking.pangya.login.model.ConnectionState;
import work.fking.pangya.login.packet.inbound.CheckNicknamePacket;
import work.fking.pangya.login.packet.inbound.LoginRequestPacket;
import work.fking.pangya.test.PacketHandlerTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testing LoginPacketHandler")
@TestInstance(Lifecycle.PER_CLASS)
public class LoginPacketHandlerTest {

    private EmbeddedChannel channel;

    @BeforeEach
    public void beforeEach() {
        var discoveryClient = mock(DiscoveryClient.class);

        when(discoveryClient.instances(ServerType.GAME)).thenReturn(List.of());
        when(discoveryClient.instances(ServerType.SOCIAL)).thenReturn(List.of());

        var handler = new LoginPacketHandler(discoveryClient);
        channel = new EmbeddedChannel(PacketHandlerTestUtils.channelHandlerFor(handler));
    }

    @AfterEach
    public void afterAll() {
        channel.disconnect();
    }

    @Test
    @DisplayName("Should disconnect if the state is invalid")
    public void test_invalid_state() {
        var packet = new LoginRequestPacket("username", "password".toCharArray());
        channel.writeInbound(packet);

        assertFalse(channel.isActive());
    }

    @Test
    @DisplayName("Should not disconnect and should return a response")
    public void test_valid_state() {
        channel.attr(ConnectionState.KEY).set(ConnectionState.AUTHENTICATING);
        var packet = new LoginRequestPacket("username", "password".toCharArray());
        channel.writeInbound(packet);

        var outboundPacket = channel.readOutbound();

        assertTrue(channel.isActive());
        assertNotNull(outboundPacket);
    }
}
