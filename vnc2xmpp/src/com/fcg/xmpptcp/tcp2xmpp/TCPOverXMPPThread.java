package com.fcg.xmpptcp.tcp2xmpp;

import java.net.Socket;
import java.util.Date;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.fcg.xmpptcp.common.ConnectionUtil;
import com.fcg.xmpptcp.common.TCP2XMPPPumpThread;
import com.fcg.xmpptcp.common.XMPP2TCPPumpThread;

public class TCPOverXMPPThread extends Thread {
	private Socket socket = null;
	private XMPPConnection connection = null;
	private Chat chat;
	private String resource = "TCPOverXMPPThread_" + (new Date()).getTime();
	
	public TCPOverXMPPThread(Socket socket, String gtalkUsr, String gtalkPwd, String recipientId) throws XMPPException {
		super("TCPOverXMPPThread");
		this.socket = socket;
		XMPPConnection con = ConnectionUtil.getGoogleConnection(); //new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
		con.login(gtalkUsr, gtalkPwd, resource);
		this.connection = con;
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
			System.out.println("CLOSED connection of resource :" + resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}