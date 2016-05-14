package org.jmatrix.proxy.core.backend;

import java.net.SocketAddress;

/**
 * @author jmatrix
 * @date 16/5/8
 */
abstract public class AbstractEndpoint {

    private SocketAddress socketAddress;

    private String hostname;

    private int port;

    public AbstractEndpoint(SocketAddress socketAddress){
        this.socketAddress = socketAddress;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
