package org.jmatrix.proxy.redis;

import io.netty.channel.ChannelHandlerContext;
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
        if (commandName.equalsIgnoreCase("set")) {
            ctx.writeAndFlush(SetCommandResult.successResult());
        } else if (commandName.equalsIgnoreCase("get")) {
            ctx.writeAndFlush(GetCommandResult.emptyResult());
        }
    }

}
