package org.jmatrix.proxy.core.backend;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.jmatrix.proxy.core.Configuration;
import org.jmatrix.proxy.core.threadpool.NamedThreadFactory;

import java.net.SocketAddress;

/**
 * @author jmatrix
 * @date 16/4/3
 */
abstract public class BackendClient extends AbstractEndpoint {

    private Bootstrap clientBootstrap;

    private NioEventLoopGroup clientGroup;

    private volatile Channel channel;

    public BackendClient(SocketAddress socketAddress) {
        super(socketAddress);
    }

    private void open() {
        clientGroup = new NioEventLoopGroup(Configuration.getIntProperty("proxy.backend.thread", 1), new NamedThreadFactory
                ("backend-client"));
        clientBootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("clientLog", new LoggingHandler(LogLevel.INFO));
                        addChannelHandler(ch);

                        ch.pipeline().addLast("clientHandler", new BackendClientHandler());
                    }
                });
    }

    private void connect() {
        if (channel != null || channel.isActive())
            return;

        ChannelFuture channelFuture = clientBootstrap.connect(getSocketAddress());
        try {
            this.channel = channelFuture.sync().channel();
        } catch (InterruptedException e) {

        }
    }

    public void send(){

    }

    private void close() {
        if (clientGroup != null && !clientGroup.isShutdown())
            clientGroup.shutdownGracefully();
    }

    abstract protected void addChannelHandler(NioSocketChannel ch);
}
