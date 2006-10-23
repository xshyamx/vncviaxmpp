package com.fcg.xmppvnc.vnc2xmpp;

import java.net.Socket;

import org.jivesoftware.smack.XMPPConnection;

public class TCP2XMPPPumpThread extends Thread {
	private Socket socket = null;
	private XMPPConnection connection = null;
	private String recipientId;
	
	public TCP2XMPPPumpThread(Socket socket, XMPPConnection connection, String recipientId) {
		this.socket = socket;
		this.connection = connection;
		this.recipientId = recipientId; 
	}
	
	public void run() {
		/* TODO: copy from socket to connection */
		// Create Chat and send msg , do we need to synchronize ?
		/*
		// sample code: 
		 XMPPConnection.DEBUG_ENABLED = true;

		XMPPConnection connection = new SSLXMPPConnection("talk.google.com",
				443, "gmail.com");
		connection.login("hathanhthai", "purplecat809");
		Chat chat = connection.createChat("hathanhthai2@gmail.com");
		chat.sendMessage("Howdy2!");
		connection.close();
		 */
	}
}
