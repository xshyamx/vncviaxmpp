package com.fcg.xmppvnc.vnc2xmpp;

import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.codec.binary.Hex;
import org.jivesoftware.smack.Chat;

public class XMPP2TCPPumpThread extends Thread  {
	private Socket socket = null;
	private Chat chat = null;

	public XMPP2TCPPumpThread(Chat chat, Socket socket) {
		this.socket = socket;
		this.chat = chat;
	}

	public void run() {
		try {
			OutputStream os = this.socket.getOutputStream();
			do {
				String message = chat.nextMessage().getBody();
				if (message.equals("_begin")) continue;
				if (message.equals("_end")) break;
				byte[] bytes = Hex.decodeHex(message.toCharArray());
				os.write(bytes);
			} while (true);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
