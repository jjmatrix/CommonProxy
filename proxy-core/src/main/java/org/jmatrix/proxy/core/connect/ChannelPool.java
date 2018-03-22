package org.jmatrix.proxy.core.connect;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jmatrix on 2017/6/22.
 */
public class ChannelPool {

    private ConcurrentHashMap<Endpoint, LinkedBlockingDeque<Channel>> idlePoolChannel = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Endpoint, LinkedBlockingDeque<Channel>> allPoolChannel = new ConcurrentHashMap<>();

    private ReentrantLock channelLock = new ReentrantLock();

    private ChannelFactory channelFactory;

    public ChannelPool() {
        channelFactory = new ChannelFactory();
        channelFactory.init();
    }

    public Channel borrowChannel(Endpoint endpoint) {
        if (endpoint == null) {
            throw new NullPointerException("endpoint cant be null.");
        }

        Channel channel = null;
        if (idlePoolChannel.get(endpoint) == null || idlePoolChannel.get(endpoint).isEmpty()) {
            channel = channelFactory.acquireChannelSync(endpoint);
            if (channel != null) {
                LinkedBlockingDeque<Channel> allChannelQueue = allPoolChannel.get(endpoint);
                if (allChannelQueue == null) {
                    allChannelQueue = new LinkedBlockingDeque<>();
                    allPoolChannel.put(endpoint, allChannelQueue);
                }
                allChannelQueue.addFirst(channel);
            }

        } else {
            channel = idlePoolChannel.get(endpoint).pollFirst();
        }

        return channel;
    }

    public void returnChannel(Endpoint endpoint, Channel channel) {
        LinkedBlockingDeque<Channel> idleChannelQueue = idlePoolChannel.get(endpoint);
        if (idleChannelQueue == null) {
            idleChannelQueue = new LinkedBlockingDeque<>();
            idlePoolChannel.put(endpoint, idleChannelQueue);
        }
        idleChannelQueue.addFirst(channel);
    }

    
}
