package org.jmatrix.proxy.core.mock;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author jmatrix
 * @date 16/4/29
 */
public interface Visitor {

    void visitChannelAccept(ChannelHandlerContext ctx, Object msg);

    void visitChannelRead(ChannelHandlerContext ctx, Object msg);

    void visitChannelWrite(ChannelHandlerContext ctx, Object msg);

}
