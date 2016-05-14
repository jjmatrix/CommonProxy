package org.jmatrix.proxy.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.jmatrix.proxy.core.Configuration;
import org.jmatrix.proxy.core.Dispatcher;
import org.jmatrix.proxy.core.ExtendLoader;
import org.jmatrix.proxy.core.ProxyHandler;
import org.jmatrix.proxy.core.threadpool.NamedThreadFactory;
import org.jmatrix.proxy.redis.codec.RedisDecoder;
import org.jmatrix.proxy.redis.codec.RedisEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy example
 *
 * @author jmatrix
 * @date 16/4/3
 */
public class ProxyExample {

    private Logger logger = LoggerFactory.getLogger(ProxyExample.class);

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;

    private ExtendLoader<Dispatcher> dispatcherExtendLoader = ExtendLoader.load(Dispatcher.class);

    public void start() {
        bossGroup = new NioEventLoopGroup(Configuration.getIntProperty("proxy.nio.reactor.thread", 1), new NamedThreadFactory
                ("proxy-reactor"));
        workGroup = new NioEventLoopGroup(Configuration.getIntProperty("proxy.nio.work.thread", Runtime.getRuntime().availableProcessors() * 2),
                new NamedThreadFactory("proxy-work"));

        ExampleHandler exampleHandler = new ExampleHandler();
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("log", new LoggingHandler(LogLevel.INFO));
                            ch.pipeline().addLast("idle", new IdleStateHandler(Configuration.getIntProperty("proxy.channel.timeout", 30),
                                    Configuration.getIntProperty("proxy.channel.timeout", 30), 0));
                            ch.pipeline().addLast("decodec", new RedisDecoder());
                            ch.pipeline().addLast("encodec", new RedisEncoder());
                            ch.pipeline().addLast("example", exampleHandler);
                            ch.pipeline().addLast("handler", new ProxyHandler(dispatcherExtendLoader.acquireProvider("redis", false)));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, Configuration.getIntProperty("proxy.maxConnect", 100))
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_TIMEOUT, Configuration.getIntProperty("proxy.timeout", 3000))
                    .childOption(ChannelOption.SO_KEEPALIVE, Configuration.getBooleanProperty("proxy.channel.keepAlive", true))
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true);

            channelFuture = serverBootstrap.bind(Configuration.getIntProperty("proxy.port", 2181)).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            logger.error("start server error.", e);
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void stop() {
        try {
            if (!workGroup.isShutdown()) {
                workGroup.shutdownGracefully();
            }
            if (!bossGroup.isShutdown()) {
                bossGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            logger.error("shutdown error.");
        }
    }
}
