package com.fcg.xmppvnc.vnc2xmpp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class KKMultiServerThread extends Thread {
	private Socket socket = null;

	public KKMultiServerThread(Socket socket) {
		super("KKMultiServerThread");
		this.socket = socket;
	}
 
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));

			String inputLine, outputLine;

			outputLine = processInput(null);
			out.println(outputLine);

			while ((inputLine = in.readLine()) != null) {
				outputLine = processInput(inputLine);
				out.println(outputLine);
				if (outputLine.equals("Bye"))
					break;
			}
			out.close();
			in.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String processInput(Object object) throws XMPPException {
		XMPPConnection.DEBUG_ENABLED = true;

		XMPPConnection connection = new SSLXMPPConnection("talk.google.com",
				443, "gmail.com");
		connection.login("hathanhthai", "purplecat809");
		Chat chat = connection.createChat("hathanhthai2@gmail.com");
		chat.sendMessage("Howdy2!");
		connection.close();
		return null;
	}
}