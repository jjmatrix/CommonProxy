package org.jmatrix.proxy.redis.codec;

import io.netty.buffer.ByteBuf;

/**
 * Temporary interface, isn't common
 *
 * @author jmatrix
 * @date 16/4/20
 */
public interface CommandResult {

    /**
     * write command response
     *
     * @param byteBuf
     */
    void write(ByteBuf byteBuf);
}
