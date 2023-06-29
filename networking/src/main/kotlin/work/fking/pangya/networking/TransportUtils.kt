package work.fking.pangya.networking

import io.netty.channel.EventLoopGroup
import io.netty.channel.ServerChannel
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

fun selectBestServerChannelAvailable(): Class<out ServerChannel?> {
    return if (Epoll.isAvailable()) EpollServerSocketChannel::class.java else NioServerSocketChannel::class.java
}

fun selectBestEventLoopAvailable(threads: Int = 1): EventLoopGroup {
    return if (Epoll.isAvailable()) EpollEventLoopGroup(threads) else NioEventLoopGroup(threads)
}
