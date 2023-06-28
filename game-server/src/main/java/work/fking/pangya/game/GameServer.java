package work.fking.pangya.game;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.game.net.ServerChannelInitializer;
import work.fking.pangya.game.player.Item;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.networking.SimpleServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class GameServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServer.class);

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private final AtomicInteger connectionIdSequence = new AtomicInteger();
    private final AtomicInteger playerCount = new AtomicInteger();
    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private final ServerConfig serverConfig;
    private final SessionClient sessionClient;
    private final List<ServerChannel> serverChannels;

    public GameServer(ServerConfig serverConfig, SessionClient sessionClient, List<ServerChannel> serverChannels) {
        this.serverConfig = serverConfig;
        this.sessionClient = sessionClient;
        this.serverChannels = serverChannels;
    }

    public int playerCount() {
        return playerCount.get();
    }

    public List<ServerChannel> serverChannels() {
        return Collections.unmodifiableList(serverChannels);
    }

    public ServerChannel serverChannelById(int id) {
        for (var serverChannel : serverChannels) {
            if (serverChannel.id() == id) {
                return serverChannel;
            }
        }
        return null;
    }

    public SessionClient sessionClient() {
        return sessionClient;
    }

    public void submitTask(Runnable runnable) {
        executorService.execute(runnable);
    }

    public Player findPlayer(int connectionId) {
        return players.get(connectionId);
    }

    public Player registerPlayer(Channel channel, int uid, String nickname) {
        var player = new Player(channel, uid, connectionIdSequence.incrementAndGet(), nickname);

        var characterRoster = player.characterRoster();
        characterRoster.unlockCharacter(67108872);

        var caddieRoster = player.caddieRoster();
        caddieRoster.unlockCaddie(469762048);

        var inventory = player.inventory();

        inventory.add(Item.create(268435511)); // clubset
        inventory.add(Item.create(335544382, 100)); // comets
        inventory.add(Item.create(436207656, 100)); // papel shop coupons

        // lucia items
        inventory.add(Item.create(136331265));
        inventory.add(Item.create(136423465));
        inventory.add(Item.create(136456205));
        inventory.add(Item.create(136456192));

        var equipment = player.equipment();
        equipment.equipCharacter(characterRoster.findByIffId(67108872));
        equipment.equipCaddie(caddieRoster.findByIffId(469762048));
        equipment.equipClubSet(inventory.findByIffId(268435511));
        equipment.equipComet(inventory.findByIffId(335544382));

        players.put(player.connectionId(), player);
        playerCount.incrementAndGet();
        channel.closeFuture().addListener(v -> onPlayerDisconnect(player));
        return player;
    }

    private void onPlayerDisconnect(Player player) {
        var channel = player.currentChannel();

        if (channel != null) {
            channel.removePlayer(player);
        }
        players.remove(player.connectionId());
        playerCount.decrementAndGet();
        LOGGER.debug("{} disconnected", player.nickname());
    }

    public void start() throws IOException, InterruptedException {
        var channelInitializer = ServerChannelInitializer.create(this);

        var server = SimpleServer.builder()
                                 .channelInitializer(channelInitializer)
                                 .address(InetAddress.getByName(serverConfig.bindAddress()))
                                 .port(serverConfig.port())
                                 .build();
        server.start();
    }
}
