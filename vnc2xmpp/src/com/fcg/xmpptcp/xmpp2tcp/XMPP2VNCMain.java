package com.fcg.xmpptcp.xmpp2tcp;

import java.net.Socket;
import java.util.Hashtable;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;


public class XMPP2VNCMain {
	private static String gtalkUsr = "xmppserver";

	private static String gtalkPwd = "thaiha";

	private static XMPPConnection con;

	private static Hashtable threadIds = new Hashtable();

	public static void main(String[] args) throws XMPPException {
		XMPPConnection.DEBUG_ENABLED = true;

		con = new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
		con.login(gtalkUsr, gtalkPwd);

		// Accept only messages from client
		PacketFilter filter = new AndFilter(
				new PacketTypeFilter(Message.class), new FromContainsFilter(
						"xmppclient@gmail.com"));

		PacketListener myListener = new PacketListener() {
			public void processPacket(Packet packet) {
				try {
					if (packet instanceof Message) {
						Message msg = (Message) packet;
						synchronized(threadIds) {
							System.out.println("see thread id:" + msg.getThread());
							if (!threadIds.containsKey(msg.getThread())) {
								threadIds.put(msg.getThread(), "true");
								/* a message of a brand new thread */
								Chat chat = new Chat(con, "xmppclient@gmail.com", msg.getThread());
								Socket socket = new Socket("localhost", 23);
								System.out.println("Chat not exist, create new tunnel");
								new XMPPOverTCPThread(socket, chat).start();
							} else {
								System.out.println("Existing tunnel");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		// Register the listener.
		con.addPacketListener(myListener, filter);

	}
}
