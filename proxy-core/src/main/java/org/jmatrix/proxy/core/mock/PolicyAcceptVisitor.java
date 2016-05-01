package org.jmatrix.proxy.core.mock;

import io.netty.channel.ChannelHandlerContext;
import org.jmatrix.proxy.core.Configuration;
import org.jmatrix.proxy.core.exception.OperationNotSupportException;

/**
 * @author jmatrix
 * @date 16/4/29
 */
public class PolicyAcceptVisitor extends AbstractPolicyVisitor {

    @Override
    public void visitChannelRead(ChannelHandlerContext ctx, Object msg) {
        throw new OperationNotSupportException("ChannelRead not allow.");
    }

    @Override
    public void visitChannelWrite(ChannelHandlerContext ctx, Object msg) {
        throw new OperationNotSupportException("ChannelWrite not allow.");
    }

    @Override
    protected long acceptLazy() {
        return Configuration.getLongProperty("proxy.policy.acceptTime", 1000);
    }
}
