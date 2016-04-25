package org.jmatrix.proxy.core.threadpool;

import java.util.concurrent.TimeUnit;

/**
 * Thread pool
 *
 * @author jmatrix
 * @date 16/4/17
 */
public class ThreadPoolConfig {

    private int corePoolSize;

    private int maxPoolSize;

    private int queueSize;

    private long keepAliveTime;

    private TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;

    public ThreadPoolConfig() {
        this.corePoolSize = 2;
        this.maxPoolSize = Runtime.getRuntime().availableProcessors();
        this.queueSize = 100;
        this.keepAliveTime = 5;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public TimeUnit getKeepAliveTimeUnit() {
        return keepAliveTimeUnit;
    }

    public void setKeepAliveTimeUnit(TimeUnit keepAliveTimeUnit) {
        this.keepAliveTimeUnit = keepAliveTimeUnit;
    }


}
