package org.javavnc.common;

import junit.framework.TestCase;

public class VNCManagerTest extends TestCase {

    public void testStartVNCServer() throws Exception {
        VNCManager manager = new VNCManager();
        Process p = manager.startVNCServer();
        assertNotNull(p);
        p.destroy();
    }

    public void testStartVNCClient() throws Exception {
        VNCManager manager = new VNCManager();
        Process p = manager.startVNCClient("localhost", 5222);
        assertNotNull(p);
        p.destroy();
    }
}
