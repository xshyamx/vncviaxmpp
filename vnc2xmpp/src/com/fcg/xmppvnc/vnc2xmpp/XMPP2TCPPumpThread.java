package com.fcg.xmppvnc.vnc2xmpp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

public class XMPP2TCPPumpThread extends Thread  {
	private Socket socket = null;
	private XMPPConnection connection = null;
	private String recipientId;

	public XMPP2TCPPumpThread(XMPPConnection connection, String recipientId, Socket socket) {
		this.socket = socket;
		this.connection = connection;
		this.recipientId = recipientId;
	}
	public void run() {
		try {
			OutputStream os = this.socket.getOutputStream();
			OutputStream bos = new BufferedOutputStream(os);
			
			PacketFilter filter = new FromContainsFilter(recipientId);
			PacketCollector myCollector = connection.createPacketCollector(filter);
			Packet p;
			do {
				p = myCollector.nextResult();
				if (p instanceof Presence) {
					Presence presence = (Presence) p;
					if (presence.getType() == Presence.Type.UNAVAILABLE) {
						break;
					}
				} else if (p instanceof Message) {
					Message m = (Message) p;
					String hexStr = m.getBody();
					//TODO decode and send to socket
				}
			} while (true);
			bos.close();
			os.close();
			this.connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
