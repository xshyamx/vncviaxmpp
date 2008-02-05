package org.javavnc.xmpp2tcp;

import java.net.Socket;

import org.jivesoftware.smack.Chat;

import org.javavnc.common.TCP2XMPPPumpThread;
import org.javavnc.common.XMPP2TCPPumpThread;

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
