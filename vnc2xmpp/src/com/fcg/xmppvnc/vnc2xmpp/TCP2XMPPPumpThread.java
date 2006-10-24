package com.fcg.xmppvnc.vnc2xmpp;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.Socket;

import org.apache.commons.codec.binary.Hex;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;

public class TCP2XMPPPumpThread extends Thread {
	private Socket socket = null;

	private XMPPConnection connection = null;

	private String recipientId;

	public TCP2XMPPPumpThread(Socket socket, XMPPConnection connection,
			String recipientId) {
		this.socket = socket;
		this.connection = connection;
		this.recipientId = recipientId;
	}

	public void run() {
		XMPPConnection.DEBUG_ENABLED = true;
		try {
			Chat chat = connection.createChat(recipientId);
			InputStream is = this.socket.getInputStream();
			InputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[4*1024];
			int read;
			do {
				read = bis.read(buffer);
				if (read > 0) {
					byte[] newArray = (byte[]) Array.newInstance(buffer
							.getClass().getComponentType(), read);
					System.arraycopy(buffer, 0, newArray, 0, read);
					char[] text = Hex.encodeHex(newArray);
					chat.sendMessage(new String(text));
				}
			} while (read >= 0);
			bis.close();
			is.close();
			connection.close();
			System.out.println("CLOSED!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
