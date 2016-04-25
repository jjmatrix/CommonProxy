package org.jmatrix.proxy.core;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jmatrix
 * @date 16/4/12
 */
public class Request<T> {

    private static AtomicLong idGenerator = new AtomicLong(0);

    private long id;

    private long version = 20160121L;

    private T data;

    public Request() {
        id = nextId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private long nextId() {
        return idGenerator.incrementAndGet();
    }
}
