package com.fcg.xmppvnc.vnc2xmpp;

import java.net.Socket;

import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class TCPOverXMPPThread extends Thread {
	private Socket socket = null;
	private XMPPConnection connection = null;
	private String recipientId;
	
	public TCPOverXMPPThread(Socket socket, String gtalkUsr, String gtalkPwd, String recipientId) throws XMPPException {
		super("TCPOverXMPPThread");
		this.socket = socket;
		XMPPConnection con = new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
		con.login(gtalkUsr, gtalkPwd);
		this.connection = con;
		this.recipientId = recipientId;
	}
 
	public void run() {
		try {
			Thread t1 = new TCP2XMPPPumpThread(socket, connection, recipientId);
			Thread t2 = new XMPP2TCPPumpThread(connection, recipientId, socket);
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			socket.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}