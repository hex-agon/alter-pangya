package work.fking.pangya.game.net

import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import work.fking.pangya.game.GameServer
import work.fking.pangya.networking.protocol.SimplePacketEncoder

class ServerChannelInitializer(gameServer: GameServer) : ChannelInitializer<Channel>() {
    private val helloHandler: HelloHandler

    init {
        helloHandler = HelloHandler(gameServer)
    }

    override fun initChannel(channel: Channel) {
        val pipeline = channel.pipeline()
        pipeline.addLast("encoder", SimplePacketEncoder())
        pipeline.addLast(helloHandler)
    }
}
