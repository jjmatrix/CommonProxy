package org.jmatrix.proxy.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jmatrix
 * @date 16/5/18
 */
public class ExecutorCache {

    private ExecutorService executorService;

    public ExecutorCache() {
        executorService = Executors.newFixedThreadPool(10);
    }

    public void executeTask(Runnable runnable){
        executorService.execute(runnable);
    }

    public void destroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

}
