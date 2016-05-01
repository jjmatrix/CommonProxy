package org.jmatrix.proxy.core.threadpool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Thread Pool Factory
 *
 * @author jmatrix
 * @date 16/4/20
 */
public class DefaultThreadPoolFactory {

    private final static String DEFAULT_POOL = "default";

    private static Map<String, ThreadPoolConfig> threadPoolConfigMap = new ConcurrentHashMap<>();

    public static ThreadPoolExecutor createThreadPool(String poolName) {
        ThreadPoolConfig poolConfig = threadPoolConfigMap.get(poolName);
        if (poolConfig == null) {
            //Use default thread pool
            poolConfig = threadPoolConfigMap.get(DEFAULT_POOL);
        }
        if (poolConfig == null) {
            poolConfig = new ThreadPoolConfig();
            threadPoolConfigMap.put(DEFAULT_POOL, poolConfig);
        }
        return createThreadPool(poolName, poolConfig);
    }

    public static ThreadPoolExecutor createThreadPool(String poolName, ThreadPoolConfig poolConfig) {
        return new ThreadPoolExecutor(poolConfig.getCorePoolSize(), poolConfig.getMaxPoolSize(), poolConfig.getKeepAliveTime(),
                poolConfig.getKeepAliveTimeUnit(), new LinkedBlockingDeque<>(poolConfig.getQueueSize()), new NamedThreadFactory
                (poolName));
    }

}
