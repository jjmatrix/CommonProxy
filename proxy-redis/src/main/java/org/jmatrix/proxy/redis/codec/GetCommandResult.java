package org.jmatrix.proxy.redis.codec;

import io.netty.buffer.ByteBuf;
import org.jmatrix.proxy.redis.RedisProtocol;

/**
 * @author jmatrix
 * @date 16/4/20
 */
public class GetCommandResult implements CommandResult {

    private final static String EMPTY_RESULT = "$-1\r\n";
    private final static String EMPTY_RESULT_T = "";

    private String content;

    public GetCommandResult(String content) {
        this.content = content;
    }

    public static GetCommandResult emptyResult() {
        return new GetCommandResult(EMPTY_RESULT);
    }

    public static GetCommandResult emptyResultT() {
        return new GetCommandResult(EMPTY_RESULT_T);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        if (this.content.equals(EMPTY_RESULT)) {
            byteBuf.writeBytes(content.getBytes());
        } else if (this.content.equals(EMPTY_RESULT_T)) {
            byteBuf.writeBytes(content.getBytes());
        } else {
            byteBuf.writeByte(RedisProtocol.SIZE_BYTE);
            byteBuf.writeBytes(String.valueOf(content.length()).getBytes());
            byteBuf.writeBytes(RedisProtocol.CRLF);
            byteBuf.writeBytes(content.getBytes());
            byteBuf.writeBytes(RedisProtocol.CRLF);
        }
    }
}
