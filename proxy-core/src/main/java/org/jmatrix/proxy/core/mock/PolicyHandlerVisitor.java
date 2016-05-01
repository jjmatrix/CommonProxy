package org.jmatrix.proxy.core.mock;

import io.netty.channel.ChannelHandlerContext;
import org.jmatrix.proxy.core.Configuration;
import org.jmatrix.proxy.core.exception.OperationNotSupportException;

/**
 * @author jmatrix
 * @date 16/5/1
 */
public class PolicyHandlerVisitor extends AbstractPolicyVisitor {

    @Override
    public void visitChannelAccept(ChannelHandlerContext ctx, Object msg) {
        throw new OperationNotSupportException("ChannelAccept not allow.");
    }

    @Override
    protected long readLazy() {
        return Configuration.getLongProperty("proxy.policy.readTime", 1000);
    }

    @Override
    protected long writeLazy() {
        return Configuration.getLongProperty("proxy.policy.writeTime", 1000);
    }
}
