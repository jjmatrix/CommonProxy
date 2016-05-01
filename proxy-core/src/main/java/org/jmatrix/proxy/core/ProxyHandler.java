package org.jmatrix.proxy.core;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.jmatrix.proxy.core.mock.Visitor;
import org.jmatrix.proxy.core.mock.VisitorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * Proxy Core Handler
 *
 * @author jmatrix
 * @date 16/4/3
 */
public class ProxyHandler extends ChannelHandlerAdapter implements ChannelInboundHandler, ChannelOutboundHandler {

    private Logger logger = LoggerFactory.getLogger(ProxyHandler.class);

    private Dispatcher dispatcher;

    private static Visitor visitor = VisitorFactory.createVisitor(VisitorFactory.VisitorType.HANDLER);

    public ProxyHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("register channel:{}.", ctx.channel().localAddress());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("unregister channel:{}.", ctx.channel().localAddress());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("receive msg:{}", msg);
        visitor.visitChannelRead(ctx, msg);

        dispatcher.dispatch(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channel read complete.");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        if (event instanceof IdleStateEvent) {
            //if connect idle, then close the channel
            ctx.close();
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    }

    /**
     * Handler exception, otherwise the exception will forward to next ChannelHandler in the pipeline,<br/>
     * that is Tail ChannelHandler.
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    /**
     * Outbound handler
     *
     * @param ctx
     * @param localAddress
     * @param promise
     * @throws Exception
     */
    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {

    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
            throws Exception {

    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {

    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {

    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {

    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        logger.debug("[outbound]write msg:{}", msg);
        visitor.visitChannelWrite(ctx, msg);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {

    }
}
