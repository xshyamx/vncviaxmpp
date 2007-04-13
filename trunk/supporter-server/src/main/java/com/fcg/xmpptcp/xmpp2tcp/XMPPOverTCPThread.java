package com.fcg.xmpptcp.xmpp2tcp;

import java.net.Socket;

import org.jivesoftware.smack.Chat;

import com.fcg.xmpptcp.common.TCP2XMPPPumpThread;
import com.fcg.xmpptcp.common.XMPP2TCPPumpThread;

public class XMPPOverTCPThread extends Thread {

	private Socket socket = null;
	private Chat chat;

	public XMPPOverTCPThread(Socket socket, Chat chat) {
		super("XMPPOverTCPThread");
		this.socket = socket;
		this.chat = chat;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
