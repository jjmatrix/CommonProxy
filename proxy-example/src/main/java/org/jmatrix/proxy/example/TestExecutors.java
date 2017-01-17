package org.jmatrix.proxy.example;

import java.util.concurrent.locks.LockSupport;

/**
 * @author jmatrix
 * @date 16/5/18
 */
public class TestExecutors {

    private static ExecutorCache executorCache;

    public static void test(){
        executorCache = new ExecutorCache();
        for (int i = 0; i < 3; i++) {
            executorCache.executeTask(() -> {
                LockSupport.parkNanos(1000 * 1000);
            });
        }
//            while (true) {
//
//            }
//            while (true) {
//                LockSupport.parkNanos(1000 * 1000 * 3);
//            }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            executorCache = new ExecutorCache();
            for (int i = 0; i < 3; i++) {
                executorCache.executeTask(() -> {
                    LockSupport.parkNanos(1000 * 1000);
                });
            }
        });
        thread.setDaemon(false);
        thread.start();

        try {
            Thread.sleep(1000 * 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
