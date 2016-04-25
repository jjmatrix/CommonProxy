package org.jmatrix.proxy.http;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.jmatrix.proxy.core.Configuration;
import org.jmatrix.proxy.core.ProxyChannelInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmatrix
 * @date 16/4/24
 */
public class HttpChannelInitializer extends ProxyChannelInitializer {

    private Logger logger = LoggerFactory.getLogger(HttpChannelInitializer.class);

    private static SslContext sslContext;

    private static Object sslLock = new Object();

    @Override
    protected void addExtendChannelHandler(SocketChannel ch) {
        if (Configuration.getBooleanProperty("proxy.http.ssl", false)) {
            if (sslContext == null) {
                synchronized (sslLock) {
                    if (sslContext == null)
                        sslContext = createSslContext();
                }
            }
            if (sslContext != null) {
                ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));
            }
        }
        ch.pipeline().addLast("serverCodec", new HttpServerCodec());
        ch.pipeline().addLast("objectAggr", new HttpObjectAggregator(1048576));
    }

    private SslContext createSslContext() {
        SslContext sslContext = null;
        try {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } catch (Exception e) {
            logger.error("create ssl context failed.", e);
        }
        return sslContext;
    }
}
