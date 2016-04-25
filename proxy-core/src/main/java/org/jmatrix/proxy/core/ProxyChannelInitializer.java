package org.jmatrix.proxy.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Initialize ChannelHandler
 *
 * @author jmatrix
 * @date 16/4/23
 */
abstract public class ProxyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ExtendLoader<Dispatcher> dispatcherExtendLoader = ExtendLoader.load(Dispatcher.class);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        String preferred = Configuration.getProperty("proxy.type", "redis");
        if (Configuration.getBooleanProperty("proxy.debug", false)) {
            ch.pipeline().addLast("log", new LoggingHandler(LogLevel.INFO));
        }
        ch.pipeline().addLast("idle", new IdleStateHandler(Configuration.getIntProperty("proxy.channel.timeout", 30),
                Configuration.getIntProperty("proxy.channel.timeout", 30), 0));
        addExtendChannelHandler(ch);
        ch.pipeline().addLast("handler", new ProxyHandler(dispatcherExtendLoader.acquireProvider(preferred, false)));
    }

    /**
     * add protocol special ChannelHandler
     *
     * @param ch
     */
    abstract protected void addExtendChannelHandler(SocketChannel ch);
}
