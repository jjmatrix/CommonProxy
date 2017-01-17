package org.jmatrix.proxy.server;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.jmatrix.proxy.core.mock.Visitor;
import org.jmatrix.proxy.core.mock.VisitorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmatrix
 * @date 16/8/29
 */
public class ProxyAcceptHandler extends ChannelDuplexHandler {

    private Logger logger = LoggerFactory.getLogger(ProxyAcceptHandler.class);

    private static Visitor visitorAcceptor = VisitorFactory.createVisitor(VisitorFactory.VisitorType.ACCEPT);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("acceptor connection, register channel : {}", ctx.channel().localAddress());
        visitorAcceptor.visitChannelAccept(ctx, null);
        super.channelActive(ctx);
    }
}
