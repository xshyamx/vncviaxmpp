package com.fcg.xmppvnc.vnc2xmpp;

import java.net.Socket;

import org.jivesoftware.smack.XMPPConnection;

public class XMPP2TCPPumpThread extends Thread  {
	private Socket socket = null;
	private XMPPConnection connection = null;
	private String recipientId;

	public XMPP2TCPPumpThread(XMPPConnection connection, String recipientId, Socket socket) {
		this.socket = socket;
		this.connection = connection;
		this.recipientId = recipientId;
	}
	public void run() {
		/* TODO: copy from connection to socket*/
		// do we need to synchronize ?

	}
}
