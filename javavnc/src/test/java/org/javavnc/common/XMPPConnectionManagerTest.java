package org.javavnc.common;

import junit.framework.TestCase;

import org.jivesoftware.smack.XMPPConnection;

public class XMPPConnectionManagerTest extends TestCase {

	public void testGetXMPPConnectionWithGTalkServer() throws Exception {
		XMPPConnectionManager manager =  new XMPPConnectionManager();
		XMPPConnection con = manager.getGoogleConnection();
		assertNotNull(con);
	}

	public void testGetXMPPConnectionWithJabberServer() throws Exception {
		XMPPConnectionManager manager =  new XMPPConnectionManager();
		XMPPConnection jabberCon = manager.getXMPPConnection("jabber.org", 5222);
		assertNotNull(jabberCon);
	}

	public void testGetXMPPConnectionWithInvalidServer() throws Exception {
		XMPPConnectionManager manager =  new XMPPConnectionManager();
		try {
		    manager.getXMPPConnection("notexistserver.org", 5222);
		    fail("An exception must happen here.");
		} catch (Exception e) {
                    // expected exception
                }
	}

}
