package com.fcg.xmpptcp.common;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class ConnectionUtil {
	private static boolean isOpen(String host, int port) {
		boolean open;
		try {
			Socket sock = new Socket();
			sock.bind(null);
			sock.connect(new InetSocketAddress(host, port), 1000);
			open = true;
		} catch (Exception e) {
			open = false;
		}
		return open;
	}

	public static XMPPConnection getXMPPConnection(String host, int port) throws XMPPException {
		XMPPConnection con;
		if (host.equalsIgnoreCase("talk.google.com")) {
			con = getGoogleConnection();
		} else {
	        ConnectionConfiguration configuration = new ConnectionConfiguration(host, port);
	        con = new XMPPConnection(configuration);
		}
		return con;
	}

	private static XMPPConnection getGoogleConnection() throws XMPPException {
		XMPPConnection con = null;
        SASLAuthentication.unregisterSASLMechanism("PLAIN");
        SASLAuthentication.registerSASLMechanism(1, "PLAIN", MySASLPlainMechanism.class);
		if (isOpen("talk.google.com", 5222)) {
			ConnectionConfiguration configuration = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
			con = new XMPPConnection(configuration);
		 } else if (isOpen("talk.google.com", 443)) {
	        con = new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
		}
		
		return con;
	}
}
