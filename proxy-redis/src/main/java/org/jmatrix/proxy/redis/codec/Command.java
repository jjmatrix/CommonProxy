package org.jmatrix.proxy.redis.codec;

import io.netty.buffer.ByteBuf;
import org.jmatrix.proxy.redis.RedisProtocol;

/**
 * @author jmatrix
 * @date 16/4/3
 */
public class Command {

    private byte[] name;

    private byte argv[][];

    private int cmdLength;

    public Command() {
    }

    public Command(byte[][] commands) {
        this.cmdLength = commands.length;
        name = commands[0];
        if (commands.length > 1) {
            argv = new byte[commands.length - 1][];
            for (int i = 1; i < commands.length; i++) {
                argv[i - 1] = commands[i];
            }
        }
    }

    public byte[][] getArgv() {
        return argv;
    }

    public void setArgv(byte[][] argv) {
        this.argv = argv;
    }

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public int getCmdLength() {
        return cmdLength;
    }

    public void setCmdLength(int cmdLength) {
        this.cmdLength = cmdLength;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Command{" +
                "name=" + new String(name) +
                ", argv=[");
        for (int i = 0; i < argv.length; i++) {
            if (i > 0)
                builder.append(",");
            builder.append(new String(argv[i]));
        }
        builder.append("]}");
        return builder.toString();
    }

    public void write(ByteBuf byteBuf) {
        byteBuf.writeByte(RedisProtocol.COUNT_BYTE);
        byteBuf.writeInt(cmdLength);
        for (int i = 0; i < cmdLength; i++) {
            byteBuf.writeByte(RedisProtocol.SIZE_BYTE);
            byteBuf.writeInt(argv[i].length);
            byteBuf.writeBytes(argv[i]);
            byteBuf.writeByte(RedisProtocol.CRLF_LEN);
        }
    }
}
