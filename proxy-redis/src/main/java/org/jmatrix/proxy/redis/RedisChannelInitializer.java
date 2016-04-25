package org.jmatrix.proxy.redis;

import io.netty.channel.socket.SocketChannel;
import org.jmatrix.proxy.core.ProxyChannelInitializer;
import org.jmatrix.proxy.redis.codec.RedisDecoder;
import org.jmatrix.proxy.redis.codec.RedisEncoder;

/**
 * @author jmatrix
 * @date 16/4/23
 */
public class RedisChannelInitializer extends ProxyChannelInitializer {

    @Override
    protected void addExtendChannelHandler(SocketChannel ch) {
        ch.pipeline().addLast("decodec", new RedisDecoder());
        ch.pipeline().addLast("encodec", new RedisEncoder());
    }
}
