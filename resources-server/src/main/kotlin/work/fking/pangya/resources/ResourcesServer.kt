package work.fking.pangya.resources

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import org.apache.logging.log4j.LogManager
import work.fking.pangya.networking.selectBestEventLoopAvailable
import work.fking.pangya.networking.selectBestServerChannelAvailable
import java.net.InetAddress

object ResourcesServer {
    private val LOGGER = LogManager.getLogger(ResourcesServer::class.java)
    const val MAX_CONTENT_LENGTH = 65536
    private const val PORT = 50009

    @JvmStatic
    fun main(args: Array<String>) {
        LOGGER.info("Bootstrapping the resources server...")
        try {
            val bootstrap = ServerBootstrap()
            val bossGroup = selectBestEventLoopAvailable()
            val workerGroup = selectBestEventLoopAvailable(2)
            bootstrap.group(bossGroup, workerGroup)
                .channel(selectBestServerChannelAvailable())
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(HttpServerChannelInitializer())
            LOGGER.info("Binding to port {}...", PORT)
            try {
                val channel = bootstrap.bind(InetAddress.getByName("127.0.0.1"), PORT)
                    .sync()
                    .channel()
                LOGGER.info("Successfully bound to port {}, server bootstrap completed!", PORT)
                channel.closeFuture().sync()
            } finally {
                bossGroup.shutdownGracefully()
                workerGroup.shutdownGracefully()
            }
        } catch (e: Exception) {
            LOGGER.fatal("Failed to bootstrap the server", e)
        }
    }
}
