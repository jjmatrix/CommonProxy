package org.jmatrix.proxy.redis.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.jmatrix.proxy.redis.RedisProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.nio.cs.ext.MS874;

import java.util.List;

/**
 * Decoder by redis protocol
 *
 * @author jmatrix
 * @date 16/4/3
 */
public class RedisDecoder extends ByteToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(RedisDecoder.class);

    private byte[][] commands;

    private int argvIndex;

    private State state;

    private enum State {
        READ_HEADER_COUNT, READ_COMMAND_LENGTH, READ_COMMAND_CONTENT
    }

    public RedisDecoder() {
        clearState();
    }

    private void setState(State state) {
        this.state = state;
    }

    /**
     * Find Redis protocol CRLF position.
     *
     * @param in
     * @return
     */
    private int findEndPos(ByteBuf in) {
        int writeIndex = in.writerIndex();
        for (int i = in.readerIndex(); i < writeIndex; i++) {
            if (in.getByte(i) == RedisProtocol.LF) {
                return i;
            } else if (in.getByte(i) == RedisProtocol.CR && i < writeIndex - 1 && in.getByte(i + 1) == RedisProtocol.LF)
                return i;
        }

        logger.debug("can not find protocol end position.");

        return -1;
    }

    private int readInt(ByteBuf in) {
        byte c;
        int result = 0;
        while ((c = in.readByte()) != RedisProtocol.CR) {
            result = (result * 10) + (c - '0');
        }
        if (in.readByte() != RedisProtocol.LF)
            throw new Error("invalid command amount.");
        return result;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (this.state) {
            case READ_HEADER_COUNT:
                if (findEndPos(in) == -1)
                    return;

                if (in.readByte() != RedisProtocol.COUNT_BYTE)
                    throw new Error("protocol error. command header count must begin with *");

                commands = new byte[readInt(in)][];

                setState(State.READ_COMMAND_LENGTH);
                break;
            case READ_COMMAND_LENGTH:
                if (findEndPos(in) == -1)
                    return;

                if (in.readByte() != RedisProtocol.SIZE_BYTE)
                    throw new Error("protocol error, command length must begin with $");

                commands[argvIndex] = new byte[readInt(in)];

                setState(State.READ_COMMAND_CONTENT);
                break;
            case READ_COMMAND_CONTENT:
                if (findEndPos(in) == -1)
                    return;

                in.readBytes(commands[argvIndex]);
                in.skipBytes(RedisProtocol.CRLF_LEN);
                argvIndex++;

                setState(State.READ_COMMAND_LENGTH);
                break;
            default:
                throw new Error("invalid state with " + state.name());
        }

        if (isComplete()) {
            out.add(new Command(commands));
            clearState();
        }

    }

    /**
     * clear decoder state
     */
    private void clearState() {
        state = State.READ_HEADER_COUNT;
        argvIndex = 0;
    }

    private boolean isComplete() {
        return (!state.equals(State.READ_HEADER_COUNT) && commands != null && argvIndex == commands.length);
    }

}
