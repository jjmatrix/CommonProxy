package org.jmatrix.proxy.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.jmatrix.proxy.core.Dispatcher;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author jmatrix
 * @date 16/4/23
 */
public class HttpDispatcher extends Dispatcher {

    private final static String JSON_RESP_FORTEST = "{\"code\":0,\"msg\":\"success\"}";

    @Override
    protected void doDispatch(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof HttpRequest)) {
            return;
        }
        HttpRequest request = (HttpRequest) msg;
        if (HttpHeaders.is100ContinueExpected(request)) {
            ctx.writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
        }
        FullHttpResponse response = prepareResponse(JSON_RESP_FORTEST);
        if (!HttpHeaders.isKeepAlive(request)) {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            ctx.writeAndFlush(response);
        }
    }

    private FullHttpResponse prepareResponse(String content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer
                (content.getBytes()));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

}
