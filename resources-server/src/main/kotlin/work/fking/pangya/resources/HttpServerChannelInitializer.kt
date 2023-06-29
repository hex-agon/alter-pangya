package work.fking.pangya.resources

import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.stream.ChunkedWriteHandler

class HttpServerChannelInitializer : ChannelInitializer<Channel>() {
    private val httpServerHandler = HttpServerHandler()

    override fun initChannel(channel: Channel) {
        val pipeline = channel.pipeline()
        pipeline.addLast(HttpServerCodec())
        pipeline.addLast(HttpObjectAggregator(ResourcesServer.MAX_CONTENT_LENGTH))
        pipeline.addLast(ChunkedWriteHandler())
        pipeline.addLast(httpServerHandler)
    }
}
