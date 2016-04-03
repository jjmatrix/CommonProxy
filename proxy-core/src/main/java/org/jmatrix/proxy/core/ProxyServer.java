package org.jmatrix.proxy.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.jmatrix.proxy.core.utils.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmatrix
 * @date 16/4/3
 */
public class ProxyServer implements Server {

    private Logger logger = LoggerFactory.getLogger(ProxyServer.class);

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;

    public void start() {
        bossGroup = new NioEventLoopGroup((Runtime.getRuntime().availableProcessors() >> 1) + 1, new NamedThreadFactory("proxy-reactor"));
        workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new NamedThreadFactory("proxy-work"));
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast();
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            channelFuture = serverBootstrap.bind(80).sync();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("shutdown server.");
                close();
            }));

        } catch (Exception e) {
            logger.error("start server error.", e);
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    protected void close() {
        try {
            channelFuture.channel().closeFuture().sync();
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
