package work.fking.pangya.networking;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

@Slf4j
public class SimpleServer {

    private final ChannelInitializer<Channel> channelInitializer;
    private final Class<? extends ServerChannel> channelClass;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final InetAddress address;
    private final int port;

    private SimpleServer(ChannelInitializer<Channel> channelInitializer, Class<? extends ServerChannel> channelClass, EventLoopGroup bossGroup, EventLoopGroup workerGroup, InetAddress address, int port) {
        this.channelInitializer = channelInitializer;
        this.channelClass = channelClass;
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.address = address;
        this.port = port;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                 .channel(channelClass)
                 .childOption(ChannelOption.TCP_NODELAY, true)
                 .childHandler(channelInitializer);

        LOGGER.info("Binding to port {}...", port);
        try {
            Channel channel = bootstrap.bind(address, port)
                                       .sync()
                                       .channel();
            LOGGER.info("Successfully bound to port {}, server bootstrap completed!", port);
            channel.closeFuture()
                   .sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * Gracefully shuts down the server. This method blocks the current thread until both loop groups exit.
     *
     * @throws InterruptedException
     * @throws IllegalStateException If any of the loop groups is already shutting down.
     */
    public void shutdown() throws InterruptedException {

        if (bossGroup.isShuttingDown() || workerGroup.isShuttingDown()) {
            throw new IllegalStateException("Already shutting down");
        }
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
    }

    public static class Builder {

        private ChannelInitializer<Channel> channelInitializer;
        private int bossThreads = 2;
        private Class<? extends ServerChannel> channelClass;
        private EventLoopGroup bossGroup;
        private EventLoopGroup workerGroup;
        private InetAddress address;
        private int port = -1;

        private Builder() {
        }

        public Builder channelInitializer(ChannelInitializer<Channel> channelInitializer) {
            this.channelInitializer = channelInitializer;
            return this;
        }

        /**
         * Specifies how many threads the boss {@link EventLoopGroup} will be created with. This option is ignored if a {@link #bossEventLoopGroup(EventLoopGroup)} is specified.
         * If no value is specified and no {@link EventLoopGroup} are provided, a value of 2 threads is used.
         *
         * @param number How many threads, must be equal or bigger than 1.
         * @return This builder.
         */
        public Builder bossThreads(int number) {
            this.bossThreads = number;
            return this;
        }

        public Builder channelClass(Class<? extends ServerChannel> channelClass) {
            this.channelClass = channelClass;
            return this;
        }

        public Builder bossEventLoopGroup(EventLoopGroup bossGroup) {
            this.bossGroup = bossGroup;
            return this;
        }

        public Builder workerEventLoopGroup(EventLoopGroup bossGroup) {
            this.bossGroup = bossGroup;
            return this;
        }

        /**
         * Specifies the address that the server will be bound to.
         *
         * @param address The server address.
         * @return This builder.
         */
        public Builder address(InetAddress address) {
            this.address = address;
            return this;
        }

        /**
         * Specifies the port that the server will be bound to.
         *
         * @param port The server port.
         * @return This builder.
         */
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public SimpleServer build() {

            if (channelInitializer == null) {
                throw new IllegalStateException("The channel initializer is required");
            }

            if (bossThreads < 1) {
                throw new IllegalStateException("The boss worker threads must be equal or bigger than 1");
            }

            if (channelClass == null) {
                channelClass = TransportUtils.selectBestServerChannelAvailable();
            }

            if (bossGroup == null) {
                bossGroup = TransportUtils.selectBestEventLoopAvailable(bossThreads);
            }

            if (workerGroup == null) {
                workerGroup = TransportUtils.selectBestEventLoopAvailable();
            }

            if (address == null) {
                throw new IllegalStateException("The bind address is required");
            }

            if (port <= 0) {
                throw new IllegalStateException("The bind port must be bigger than zero");
            }
            return new SimpleServer(channelInitializer, channelClass, bossGroup, workerGroup, address, port);
        }
    }
}
