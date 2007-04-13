package com.fcg.xmpptcp.common;

import java.net.InetSocketAddress;
import java.net.Socket;

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

	public static XMPPConnection getGoogleConnection() throws XMPPException {
		XMPPConnection con = null;
		if (isOpen("talk.google.com", 5222)) {
			con = new XMPPConnection("talk.google.com", 5222, "gmail.com");
		 } else if (isOpen("talk.google.com", 443)) {
			 con = new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
		}
		
		return con;
	}
}
