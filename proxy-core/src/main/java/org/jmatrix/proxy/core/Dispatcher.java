package org.jmatrix.proxy.core;

import io.netty.channel.ChannelHandlerContext;
import org.jmatrix.proxy.core.threadpool.DefaultThreadPoolFactory;
import org.jmatrix.proxy.core.threadpool.ThreadPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jmatrix
 * @date 16/4/12
 */
abstract public class Dispatcher {

    protected Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private ThreadPoolExecutor threadPoolExecutor;

    private boolean usePool = true;

    public Dispatcher() {
        threadPoolExecutor = DefaultThreadPoolFactory.createThreadPool("proxyDispatcher");
        usePool = Configuration.getBooleanProperty("proxy.dispatcher.usePool", true);
    }

    /**
     * Dispatch request
     *
     * @param ctx
     * @param msg
     */
    public void dispatch(ChannelHandlerContext ctx, Object msg) {
        if (usePool && threadPoolExecutor != null) {
            threadPoolExecutor.execute(() -> {
                policyDispatch(ctx, msg);
            });
        } else {
            policyDispatch(ctx, msg);
        }
    }

    protected void policyDispatch(ChannelHandlerContext ctx, Object msg) {
        if (logger.isDebugEnabled()) {
            if (!ctx.executor().inEventLoop(Thread.currentThread())) {
                logger.debug("dispatch to new thread pool.");
            }
        }
        doDispatch(ctx, msg);
    }

    abstract protected void doDispatch(ChannelHandlerContext ctx, Object msg);
}
