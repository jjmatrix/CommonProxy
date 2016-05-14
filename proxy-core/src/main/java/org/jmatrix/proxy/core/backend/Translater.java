package org.jmatrix.proxy.core.backend;

import io.netty.buffer.ByteBuf;

/**
 * @author jmatrix
 * @date 16/5/14
 */
public interface Translater {
    void translate(ByteBuf byteBuf);
}
