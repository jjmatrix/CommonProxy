package org.jmatrix.proxy.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author jmatrix
 * @date 16/8/26
 */
public interface ConnectCallback {

    void onReceiveData(ByteBuf respByteBuf, ChannelHandlerContext ctx);

    void onTimeout(ChannelHandlerContext ctx);

    void onError(ChannelHandlerContext ctx);
}
