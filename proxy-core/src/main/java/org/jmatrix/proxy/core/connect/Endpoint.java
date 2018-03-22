package org.jmatrix.proxy.core.connect;

/**
 * Created by jmatrix on 2017/7/10.
 */
public class Endpoint {
    private String address;

    private int port;

    public Endpoint() {
    }

    public Endpoint(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Endpoint endpoint = (Endpoint) o;

        if (getPort() != endpoint.getPort()) return false;
        return getAddress() != null ? getAddress().equals(endpoint.getAddress()) : endpoint.getAddress() == null;

    }

    @Override
    public int hashCode() {
        int result = getAddress() != null ? getAddress().hashCode() : 0;
        result = 31 * result + getPort();
        return result;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "address='" + address + '\'' +
                ", port=" + port +
                '}';
    }
}
