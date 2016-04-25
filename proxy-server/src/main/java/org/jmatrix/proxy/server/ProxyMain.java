package org.jmatrix.proxy.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmatrix
 * @date 16/4/19
 */
public class ProxyMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyMain.class);

    private static volatile boolean running = false;

    public static void main(String[] args) {
        final ProxyServer proxyServer = new ProxyServer();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    proxyServer.stop();
                    LOGGER.info("proxy stopped!");
                } catch (Throwable t) {
                    LOGGER.error("can not stop proxy server.", t);
                }
                synchronized (ProxyMain.class) {
                    running = false;
                    ProxyMain.class.notify();
                }
            }
        });

        try {
            proxyServer.start();
            running = true;
        } catch (Exception e) {
            LOGGER.error("can not start proxy server.", e);
        }

        synchronized (ProxyMain.class) {
            while (running) {
                try {
                    ProxyMain.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }

}
