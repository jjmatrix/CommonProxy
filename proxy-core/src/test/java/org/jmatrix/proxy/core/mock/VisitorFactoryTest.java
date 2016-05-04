package org.jmatrix.proxy.core.mock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by coral on 16/5/4.
 */
public class VisitorFactoryTest {

    private ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreateVisitor() throws Exception {
        Assert.assertNotNull(VisitorFactory.createVisitor(VisitorFactory.VisitorType.HANDLER));
    }

    
}