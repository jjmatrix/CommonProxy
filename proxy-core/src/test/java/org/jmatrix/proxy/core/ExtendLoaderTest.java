package org.jmatrix.proxy.core;

import io.netty.channel.ChannelHandler;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by coral on 16/4/23.
 */
public class ExtendLoaderTest extends TestCase {


    @Test
    public void testAcquireProvider() throws Exception {
        ExtendLoader<Dispatcher> extendLoader = ExtendLoader.load(Dispatcher.class);
        Assert.assertNotNull(extendLoader.acquireProvider("test"));
    }

    @Test
    public void testAcquireProviderWithServiceName() throws Exception {
        ExtendLoader<ChannelHandler> extendLoader = ExtendLoader.load("org.jmatrix.proxy.core.decoder", ChannelHandler.class);
        Assert.assertNotNull(extendLoader.acquireProvider("test"));
    }
}