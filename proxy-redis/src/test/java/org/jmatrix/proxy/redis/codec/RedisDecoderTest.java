package org.jmatrix.proxy.redis.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coral on 16/4/9.
 */
public class RedisDecoderTest extends TestCase {

    private RedisDecoder redisDecoder = new RedisDecoder();

    @Before
    public void initMock() {
        MockitoAnnotations.initMocks(this);
    }

    private ByteBuf createRedisCommand() {
        String command = "*3\r\n" +
                "$3\r\n" +
                "set\r\n" +
                "$4\r\n" +
                "name\r\n" +
                "$10\r\n" +
                "helloworld\r\n";
        ByteBuf byteBuf = Unpooled.buffer(command.getBytes().length);
        byteBuf.writeBytes(command.getBytes());
        return byteBuf;
    }

    @Test
    public void testDecode() throws Exception {
        List<Object> commands = new ArrayList<>();
        ByteBuf byteBuf = createRedisCommand();
        long beginTimeInMs = System.currentTimeMillis();
        while (commands.isEmpty() && byteBuf.readableBytes() > 0) {
            redisDecoder.decode(null, byteBuf, commands);
            if ((System.currentTimeMillis() - beginTimeInMs) > (3000))
                break;
        }
        Assert.assertTrue((!commands.isEmpty()) && (commands.get(0) instanceof Command));
    }
}