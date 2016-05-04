package org.jmatrix.proxy.core;

import io.netty.channel.ChannelHandlerContext;

/**
 * Use for ExtendLoader unit test, as Dispatcher agent
 *
 * @author jmatrix
 * @date 16/4/23
 */
public class TestDispatcher extends Dispatcher {
    @Override
    protected void doDispatch(ChannelHandlerContext ctx, Object msg) {

    }
}
