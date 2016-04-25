package org.jmatrix.proxy.redis.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author jmatrix
 * @date 16/4/20
 */
public class RedisEncoder extends MessageToByteEncoder<CommandResult> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CommandResult msg, ByteBuf out) throws Exception {
        msg.write(out);
    }
}
