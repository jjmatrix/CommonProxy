package org.jmatrix.proxy.core;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jmatrix on 2017/6/23.
 */
public class ChannelConstant {
    public static final AttributeKey<AtomicLong> reqSequence = AttributeKey.valueOf("req_sequence");
    //    public static final AttributeKey<Map<Long, Channel>> bindChannel = AttributeKey.valueOf("bind_channel");
    public static final AttributeKey<Channel> bindChannel = AttributeKey.valueOf("bind_channel");
}
