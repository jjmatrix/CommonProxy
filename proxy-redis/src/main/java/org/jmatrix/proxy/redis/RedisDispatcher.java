package org.jmatrix.proxy.redis;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.jmatrix.proxy.core.Dispatcher;
import org.jmatrix.proxy.redis.codec.Command;
import org.jmatrix.proxy.redis.codec.GetCommandResult;
import org.jmatrix.proxy.redis.codec.SetCommandResult;

/**
 * @author jmatrix
 * @date 16/4/12
 */
public class RedisDispatcher extends Dispatcher {

    @Override
    protected void doDispatch(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof Command)) {
            return;
        }
        Command command = (Command) msg;
        String commandName = new String(command.getName());
        try {
            if (commandName.equalsIgnoreCase("set")) {
                ((ChannelOutboundHandler) ctx.handler()).write(ctx, SetCommandResult.successResult(), ctx.newPromise());
                ctx.flush();
            } else if (commandName.equalsIgnoreCase("get")) {
                ((ChannelOutboundHandler) ctx.handler()).write(ctx, GetCommandResult.emptyResult(), ctx.newPromise());
                ctx.flush();
            }
        } catch (Exception e) {

        }
    }

}
