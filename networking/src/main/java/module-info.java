module fking.work.pangya.networking {
    exports work.fking.pangya.networking;
    exports work.fking.pangya.networking.protocol;
    exports work.fking.pangya.networking.crypt;
    requires io.netty.buffer;
    requires io.netty.codec;
    requires io.netty.transport;
    requires io.netty.transport.epoll;
    requires org.apache.logging.log4j;

}