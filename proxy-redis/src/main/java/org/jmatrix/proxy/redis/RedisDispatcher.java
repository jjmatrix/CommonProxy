package org.jmatrix.proxy.redis;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.jmatrix.proxy.core.ChannelConstant;
import org.jmatrix.proxy.core.Configuration;
import org.jmatrix.proxy.core.Dispatcher;
import org.jmatrix.proxy.core.connect.ChannelPool;
import org.jmatrix.proxy.core.connect.Endpoint;
import org.jmatrix.proxy.redis.codec.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmatrix
 * @date 16/4/12
 */
public class RedisDispatcher extends Dispatcher {

    private final Logger logger = LoggerFactory.getLogger(RedisDispatcher.class);

    private ChannelPool channelPool;

    public RedisDispatcher() {
        channelPool = new ChannelPool();
    }

//    @Override
//    protected void doDispatch(ChannelHandlerContext ctx, Object msg) {
//        if (!(msg instanceof Command)) {
//            return;
//        }
//        Command command = (Command) msg;
//        String commandName = new String(command.getName());
//        try {
//            if (commandName.equalsIgnoreCase("set")) {
//                ((ChannelOutboundHandler) ctx.handler()).write(ctx, SetCommandResult.successResult(), ctx.newPromise());
//                ctx.flush();
//            } else if (commandName.equalsIgnoreCase("get")) {
//                ((ChannelOutboundHandler) ctx.handler()).write(ctx, GetCommandResult.emptyResult(), ctx.newPromise());
//                ctx.flush();
//            }
//        } catch (Exception e) {
//
//        }
//    }


    @Override
    protected void doDispatch(ChannelHandlerContext ctx, Object msg) {
        Endpoint endpoint = new Endpoint(Configuration.getProperty("proxy.backend.host"), Configuration.getIntProperty("proxy.backend.port", 0));
        Channel channel = channelPool.borrowChannel(endpoint);
        channel.attr(ChannelConstant.bindChannel).set(ctx.channel());
        if (!(msg instanceof Command)) {
            logger.warn("received msg is invalid. cls:{}", msg.getClass());
            return;
        }
        Command command = (Command) msg;
    }
}
