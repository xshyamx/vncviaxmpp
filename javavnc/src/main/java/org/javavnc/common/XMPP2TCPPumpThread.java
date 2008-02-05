package org.javavnc.common;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

public class XMPP2TCPPumpThread extends Thread  {

	private static Logger logger = Logger.getLogger(XMPP2TCPPumpThread.class) ;

	private Socket socket;
	private OutputStream os;
	private Chat chat;
	private long lastSequence;
	private boolean stop;

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

	public XMPP2TCPPumpThread(Chat chat, Socket socket) throws IOException {
		this.socket = socket;
		this.os = this.socket.getOutputStream();
		this.chat = chat;
		this.lastSequence = -1;
		this.stop = false;
	}

	public void run() {
		chat.addMessageListener(new MyMessageListener());
		while (!stop) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				logger.error("error", e);
			}
		}
	}

	private class MyMessageListener implements MessageListener {
		public void processMessage(Chat chat, Message messageObj) {
			try {
				String message = checkSequence(messageObj.getBody());
				if (message.equals("_start")) return;
				if (message.equals("_end")) {
					socket.shutdownOutput();
					stop = true;
				} else {
					byte[] bytes = StringUtils.decodeBase64(message);
					os.write(bytes);
					os.flush();
				}
			} catch (Exception e) {
				logger.error("error", e);
				stop = true;
			}
		}
	}
}
