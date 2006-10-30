package com.fcg.xmpptcp.common;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.Socket;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.util.StringUtils;

public class TCP2XMPPPumpThread extends Thread {
	private Socket socket = null;
	private Chat chat;
	private long sequence = 0;

	private String addSequence(String str) {
		return (sequence++) + ":" + str;
	}
	public TCP2XMPPPumpThread(Socket socket, Chat chat) {
		this.socket = socket;
		this.chat = chat;
	}

	public void run() {
		try {
			InputStream is = this.socket.getInputStream();
			InputStream bis = new BufferedInputStream(is);
			chat.sendMessage(addSequence("_start"));
			byte[] buffer = new byte[4*1024];
			try {
				int read;
				do {
					read = bis.read(buffer);
					if (read > 0) {
						String text = StringUtils.encodeBase64(buffer, 0, read, false);
						chat.sendMessage(addSequence(text));
					}
				} while (read >= 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			chat.sendMessage(addSequence("_end"));
			this.socket.shutdownInput();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
