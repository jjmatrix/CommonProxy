package org.jmatrix.proxy.http;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

/**
 * @author jmatrix
 * @date 16/4/24
 */
public interface RequestHandler {

    void doGet(HttpRequest request, HttpResponse response);

    void doPost(HttpRequest request, HttpResponse response);

}
