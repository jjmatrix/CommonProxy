package org.jmatrix.proxy.redis.codec;

import io.netty.buffer.ByteBuf;

/**
 * @author jmatrix
 * @date 16/4/20
 */
public class SetCommandResult implements CommandResult {

    private final static String SUC = "+OK\r\n";
    private final static String FAIL = "-FAIL\r\n";

    private String ret;

    public SetCommandResult(String ret) {
        this.ret = ret;
    }

    public static SetCommandResult successResult() {
        return new SetCommandResult(SUC);
    }

    public static SetCommandResult failResult() {
        return new SetCommandResult(FAIL);
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        byteBuf.writeBytes(ret.getBytes());
    }
}
