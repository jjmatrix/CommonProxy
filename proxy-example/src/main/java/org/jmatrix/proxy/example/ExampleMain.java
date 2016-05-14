package org.jmatrix.proxy.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmatrix
 * @date 16/4/19
 */
public class ExampleMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleMain.class);

    private static volatile boolean running = false;

    public static void main(String[] args) {
        final ProxyExample proxyExample = new ProxyExample();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    proxyExample.stop();
                    LOGGER.info("proxy stopped!");
                } catch (Throwable t) {
                    LOGGER.error("can not stop proxy server.", t);
                }
                synchronized (ExampleMain.class) {
                    running = false;
                    ExampleMain.class.notify();
                }
            }
        });

        try {
            proxyExample.start();
            running = true;
        } catch (Exception e) {
            LOGGER.error("can not start proxy server.", e);
        }

        synchronized (ExampleMain.class) {
            while (running) {
                try {
                    ExampleMain.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }

}
