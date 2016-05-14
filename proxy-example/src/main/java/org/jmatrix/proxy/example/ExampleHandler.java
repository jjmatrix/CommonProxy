package org.jmatrix.proxy.example;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jmatrix
 * @date 16/5/5
 */
@ChannelHandler.Sharable
public class ExampleHandler extends ChannelInboundHandlerAdapter implements ChannelInboundHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExampleHandler.class);

    private AttributeKey<AtomicLong> receiveMsgAmount = AttributeKey.newInstance("receiveCount");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Attribute<AtomicLong> attr = ctx.attr(receiveMsgAmount);
        AtomicLong atomicLong = attr.get();
        if (atomicLong == null) {
            atomicLong = new AtomicLong(0);
        }
        atomicLong.incrementAndGet();
        attr.set(atomicLong);
        LOGGER.debug("Test share mode of ChannelHandler,this:{},receiveMsgAmount:{},context:{}", this, atomicLong.get(), ctx);
        ctx.fireChannelRead(msg);
    }

}
