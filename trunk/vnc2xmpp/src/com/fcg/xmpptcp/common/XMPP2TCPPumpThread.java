package com.fcg.xmpptcp.common;

import java.io.OutputStream;
import java.net.Socket;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

public class XMPP2TCPPumpThread extends Thread  {
	private Socket socket = null;
	private Chat chat = null;
	private long lastSequence = -1;

	private String checkSequence(String str) {
		long thisSeq = Long.parseLong(str.substring(0, str.indexOf(':')));
		if (lastSequence == -1) lastSequence = thisSeq;
		else if (lastSequence +1 != thisSeq) {
			throw new IllegalArgumentException(str);
		} else {
			lastSequence = thisSeq;
		}
		str = str.substring(str.indexOf(':') + 1);
		return str;
	}
	
	public XMPP2TCPPumpThread(Chat chat, Socket socket) {
		this.socket = socket;
		this.chat = chat;
	}

	public void run() {
		try {
			OutputStream os = this.socket.getOutputStream();
			try {
				do {
					Message messageObj = chat.nextMessage();
					String message = checkSequence(messageObj.getBody());
					if (message.equals("_start")) continue;
					if (message.equals("_end")) break;
					byte[] bytes = StringUtils.decodeBase64(message);
					os.write(bytes);
				} while (true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.socket.shutdownOutput();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
