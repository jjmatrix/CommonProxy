package org.jmatrix.proxy.core.backend;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jmatrix
 * @date 17/1/14
 */
public class ResponseResultFuture implements ResultFuture<Response> {
    private final Lock lock = new ReentrantLock();
    private final Condition notifyCondition = lock.newCondition();

    private Response response;

    private volatile boolean reqDone = false;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        reqDone = true;
        lock.lock();
        try {
            notifyCondition.signalAll();
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Override
    public Response get() throws InterruptedException, ExecutionException {
        if (!reqDone) {
            lock.lock();
            try {
                notifyCondition.await();
            } finally {
                lock.unlock();
            }
        }
        return response;
    }

    @Override
    public Response get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!reqDone) {
            lock.lock();
            try {
                if (!notifyCondition.await(timeout, unit)) {
                    throw new TimeoutException("wait response future timeout.");
                }
            } finally {
                lock.unlock();
            }
        }
        return response;
    }
}
