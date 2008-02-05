package org.javavnc.common;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class ConnectionUtil {

	private static boolean isOpen(String host, int port) {
		boolean open;
		try {
			Socket sock = new Socket();
			sock.bind(null);
			sock.connect(new InetSocketAddress(host, port), 5000);
			sock.close();
			open = true;
		} catch (Exception e) {
			open = false;
		}
		return open;
	}

	public static XMPPConnection getXMPPConnection(String host, int port)
			throws XMPPException {

		XMPPConnection con = null;

		if (host.equalsIgnoreCase("talk.google.com")) {
			if (isOpen("talk.google.com", 5222)) {
				ConnectionConfiguration configuration =
					new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
				con = new XMPPConnection(configuration);
			} else if (isOpen("talk.google.com", 443)) {
				con = new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
			}
		}

		if (con == null) {
			ConnectionConfiguration configuration =
				new ConnectionConfiguration(host, port);
			con = new XMPPConnection(configuration);
		}
		return con;
	}

	public static XMPPConnection getGoogleConnection() throws XMPPException {
		return getXMPPConnection("talk.google.com", 5222);
	}
}
