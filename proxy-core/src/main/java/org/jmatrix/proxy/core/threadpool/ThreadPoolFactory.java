package org.jmatrix.proxy.core.threadpool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jmatrix
 * @date 16/4/17
 */
abstract public class ThreadPoolFactory {

    private static ThreadPoolFactory INSTANCE;

    public static void setInstance(final ThreadPoolFactory threadPoolFactory) {
        INSTANCE = threadPoolFactory;
    }

    public static ThreadPoolExecutor createThreadPool(String poolName) {
        return INSTANCE.create(poolName);
    }

    abstract public ThreadPoolExecutor create(String poolName);

    abstract public void loadConfig();

}
