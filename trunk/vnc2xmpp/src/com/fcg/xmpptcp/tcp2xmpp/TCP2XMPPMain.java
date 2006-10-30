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

		String port = null;
		String username = null;
		String password = null;

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			try {
				int p = Integer.parseInt(arg);
				port = String.valueOf(p);
			} catch (Exception e) {
				if (username == null) {
					username = arg;
					if (username.indexOf('@') >= 0) {
						username = username.substring(0, username.indexOf('@'));
					}
				} else {
					password = arg;
				}
			}
		}
		if (port == null) port = "5900";
		if (username == null) username = "xmppclient";
		if (password == null) password = "thaiha";
		
		System.out.println("Listen port:" + port + "; username: " + username + "; password:" + password);


		try {
			serverSocket = new ServerSocket(Integer.parseInt(port));
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}
		//usr/pwd: xmppclient@gmail.com/thaiha
		//usr/pwd: xmppserver@gmail.com/thaiha
		while (listening) {
			new TCPOverXMPPThread(serverSocket.accept(), username, password, "xmppserver@gmail.com").start();
		}
		serverSocket.close();
	}

}