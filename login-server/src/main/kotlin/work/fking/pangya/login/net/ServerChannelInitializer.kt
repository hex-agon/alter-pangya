package work.fking.pangya.login.net

import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import work.fking.pangya.login.LoginServer
import work.fking.pangya.networking.protocol.SimplePacketEncoder

class ServerChannelInitializer(
    private val helloHandler: HelloHandler,
) : ChannelInitializer<Channel>() {

    constructor(loginServer: LoginServer) : this(HelloHandler(loginServer))

    override fun initChannel(channel: Channel) {
        val pipeline = channel.pipeline()
        pipeline.addLast("encoder", SimplePacketEncoder())
        pipeline.addLast(helloHandler)
    }
}
