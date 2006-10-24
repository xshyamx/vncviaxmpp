package com.fcg.xmpptcp.tcp2xmpp;

import java.net.Socket;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.fcg.xmpptcp.common.TCP2XMPPPumpThread;
import com.fcg.xmpptcp.common.XMPP2TCPPumpThread;

public class TCPOverXMPPThread extends Thread {
	private Socket socket = null;
	private XMPPConnection connection = null;
	private String recipientId;
	private Chat chat;
	
	public TCPOverXMPPThread(Socket socket, String gtalkUsr, String gtalkPwd, String recipientId) throws XMPPException {
		super("TCPOverXMPPThread");
		this.socket = socket;
		XMPPConnection con = new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
		con.login(gtalkUsr, gtalkPwd);
		this.connection = con;
		this.recipientId = recipientId;
		chat = connection.createChat(recipientId);
	}
 
	public void run() {
		try {
			Thread t1 = new TCP2XMPPPumpThread(socket, chat);
			Thread t2 = new XMPP2TCPPumpThread(chat, socket);
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			socket.close();
			connection.close();
			System.out.println("CLOSEDMAIN!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}