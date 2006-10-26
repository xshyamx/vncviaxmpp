package com.fcg.xmpptcp.tcp2xmpp;

import java.io.IOException;
import java.net.ServerSocket;

import org.jivesoftware.smack.XMPPException;

public class TCP2XMPPMain {

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
		String port = args.length > 0 ? args[0] : "5900";
		try {
			serverSocket = new ServerSocket(Integer.parseInt(port));
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}
		//usr/pwd: xmppclient@gmail.com/thaiha
		//usr/pwd: xmppserver@gmail.com/thaiha
		while (listening) {
			new TCPOverXMPPThread(serverSocket.accept(), "xmppclient", "thaiha", "xmppserver@gmail.com").start();
		}
		serverSocket.close();
	}

}