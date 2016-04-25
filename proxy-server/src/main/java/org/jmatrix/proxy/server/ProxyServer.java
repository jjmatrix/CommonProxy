package org.jmatrix.proxy.server;

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
import org.jmatrix.proxy.core.Configuration;
import org.jmatrix.proxy.core.Dispatcher;
import org.jmatrix.proxy.core.ExtendLoader;
import org.jmatrix.proxy.core.ProxyChannelInitializer;
import org.jmatrix.proxy.core.threadpool.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmatrix
 * @date 16/4/3
 */
public class ProxyServer implements Server {

    private Logger logger = LoggerFactory.getLogger(ProxyServer.class);

    private String preferred = Configuration.getProperty("proxy.type", "redis");

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;


    private ExtendLoader<ProxyChannelInitializer> channelInitLoader = ExtendLoader.load(ProxyChannelInitializer.class);

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup(Configuration.getIntProperty("proxy.nio.reactor.thread", 1), new NamedThreadFactory
                ("proxy-reactor"));
        workGroup = new NioEventLoopGroup(Configuration.getIntProperty("proxy.nio.work.thread", Runtime.getRuntime().availableProcessors()),
                new NamedThreadFactory("proxy-work"));
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(channelInitLoader.acquireProvider(preferred))
                    .option(ChannelOption.SO_BACKLOG, Configuration.getIntProperty("proxy.maxConnect", 100))
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_TIMEOUT, Configuration.getIntProperty("proxy.timeout", 3000))
                    .childOption(ChannelOption.SO_KEEPALIVE, Configuration.getBooleanProperty("proxy.channel.keepAlive", true))
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

    @Override
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
