package work.fking.pangya.game;

import io.netty.channel.Channel;
import work.fking.pangya.common.Rand;
import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.game.net.ServerChannelInitializer;
import work.fking.pangya.game.player.Item;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.networking.SimpleServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class GameServer {

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private final AtomicInteger connectionIdSequence = new AtomicInteger();
    private final ServerConfig serverConfig;
    private final SessionClient sessionClient;

    public GameServer(ServerConfig serverConfig, SessionClient sessionClient) {
        this.serverConfig = serverConfig;
        this.sessionClient = sessionClient;
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

        return player;
    }

    public SessionClient sessionClient() {
        return sessionClient;
    }

    public void submitTask(Runnable runnable) {
        executorService.execute(runnable);
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
