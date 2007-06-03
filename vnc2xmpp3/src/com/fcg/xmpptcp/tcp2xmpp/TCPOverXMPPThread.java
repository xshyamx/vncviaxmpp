package com.fcg.xmpptcp.tcp2xmpp;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

import com.fcg.xmpptcp.common.ConnectionUtil;
import com.fcg.xmpptcp.common.TCP2XMPPPumpThread;

public class TCPOverXMPPThread extends Thread {
	private Socket socket = null;
	private Chat chat;
	private String resource = "TCPOverXMPPThread_" + (new Date()).getTime();
	
	public TCPOverXMPPThread(final Socket socket, String gtalkUsr, String gtalkPwd, String recipientId) throws XMPPException {
		super("TCPOverXMPPThread");
		this.socket = socket;
		XMPPConnection con = ConnectionUtil.getXMPPConnection("talk.google.com", 5222);
		con.connect();
		con.login(gtalkUsr, gtalkPwd, resource);
		ChatManager chatmanager = con.getChatManager();
		chat = chatmanager.createChat(recipientId, new MessageListener() {
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
			@Override
			public void processMessage(Chat chat, Message messageObj) {
				try {
					OutputStream os = socket.getOutputStream();
					try {
						do {
							String message = checkSequence(messageObj.getBody());
							if (message.equals("_start")) continue;
							if (message.equals("_end")) break;
							byte[] bytes = StringUtils.decodeBase64(message);
							os.write(bytes);
						} while (true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					socket.shutdownOutput();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
 
	public void run() {
		try {
			Thread t1 = new TCP2XMPPPumpThread(socket, chat);
			t1.start();
						
			t1.join();
			socket.close();
			System.out.println("CLOSED connection of resource :" + resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}