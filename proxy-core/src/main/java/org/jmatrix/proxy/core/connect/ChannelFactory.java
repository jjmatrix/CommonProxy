package org.jmatrix.proxy.core.connect;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.jmatrix.proxy.core.ChannelConstant;
import org.jmatrix.proxy.core.Configuration;
import org.jmatrix.proxy.core.client.ProxyClientHandler;
import org.jmatrix.proxy.core.threadpool.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jmatrix on 2017/6/22.
 */
public class ChannelFactory {

    private final Logger logger = LoggerFactory.getLogger(ChannelFactory.class);

    private Bootstrap bootstrap;

    private EventLoopGroup bossGroup;

    public void init() {
        bossGroup = new NioEventLoopGroup(Configuration.getIntProperty("connect.nio.reactor.thread", 1), new NamedThreadFactory
                ("connect-reactor"));
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_TIMEOUT, Configuration.getIntProperty("connect.timeout", 3000))
                    .option(ChannelOption.SO_KEEPALIVE, Configuration.getBooleanProperty("connect.channel.keepAlive", true))
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new ProxyClientHandler());
                        }
                    });

        } catch (Exception e) {
            logger.error("init boot strap error.", e);
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    public Channel acquireChannelSync(Endpoint endpoint) {
        ChannelFuture channelFuture = bootstrap.connect(endpoint.getAddress(), endpoint.getPort());
        Channel channel = null;
        try {
            channel = channelFuture.sync().channel();
        } catch (InterruptedException e) {
            logger.error("acquire channel failed.");
        }
        if (channel == null) {
            return null;
        }

        channel.attr(ChannelConstant.reqSequence).set(new AtomicLong(0));
        return channel;
    }
}
