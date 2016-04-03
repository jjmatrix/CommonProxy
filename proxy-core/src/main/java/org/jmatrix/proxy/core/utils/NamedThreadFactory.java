package org.jmatrix.proxy.core.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jmatrix
 * @date 16/1/22
 */
public class NamedThreadFactory implements ThreadFactory {

    private AtomicInteger threadNumber = new AtomicInteger(1);

    private ThreadGroup group;

    private String namePrefix;

    public NamedThreadFactory() {

    }

    public NamedThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix + "-thread-";
        SecurityManager sm = System.getSecurityManager();
        group = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();

    }


    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.incrementAndGet(), 0);
        t.setDaemon(true);
        return t;
    }
}
