package org.jmatrix.proxy.core.mock;

import io.netty.channel.ChannelHandlerContext;
import org.jmatrix.proxy.core.Configuration;

import java.util.concurrent.locks.LockSupport;

/**
 * @author jmatrix
 * @date 16/5/1
 */
abstract public class AbstractPolicyVisitor implements Visitor {

    private boolean policyOpen = Configuration.getBooleanProperty("proxy.policy.open", false);

    private final static long DEFAULT_LAZY_TIME_MS = 1000;

    @Override
    public void visitChannelAccept(ChannelHandlerContext ctx, Object msg) {
        policyHandler(acceptLazy());
    }

    @Override
    public void visitChannelRead(ChannelHandlerContext ctx, Object msg) {
        policyHandler(readLazy());
    }

    @Override
    public void visitChannelWrite(ChannelHandlerContext ctx, Object msg) {
        policyHandler(writeLazy());
    }

    private void policyHandler(long lazyTime) {
        if (!policyOpen)
            return;
        LockSupport.parkNanos(lazyTime * 1000);
    }

    protected long acceptLazy() {
        return DEFAULT_LAZY_TIME_MS;
    }

    protected long readLazy() {
        return DEFAULT_LAZY_TIME_MS;
    }

    protected long writeLazy() {
        return DEFAULT_LAZY_TIME_MS;
    }
}
