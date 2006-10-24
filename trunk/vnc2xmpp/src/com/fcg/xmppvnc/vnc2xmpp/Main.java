package com.fcg.xmppvnc.vnc2xmpp;

import java.io.IOException;
import java.net.ServerSocket;

import org.jivesoftware.smack.XMPPException;

public class Main {

	/**
	 * http://java.sun.com/docs/books/tutorial/networking/sockets/clientServer.html
	 * 
	 * @param args
	 * @throws XMPPException
	 * @throws IOException
	 */
	public static void main(String[] args) throws XMPPException, IOException {
		ServerSocket serverSocket = null;
		boolean listening = true;

		try {
			serverSocket = new ServerSocket(5900);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 5900.");
			System.exit(-1);
		}

		while (listening) {
			new TCPOverXMPPThread(serverSocket.accept(), "hathanhthai", "purplecat809", "hathanhthai2@gmail.com").start();
		}
		serverSocket.close();
	}

}