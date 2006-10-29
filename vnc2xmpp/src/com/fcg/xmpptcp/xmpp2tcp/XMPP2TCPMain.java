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


public class XMPP2TCPMain {
	private static String gtalkUsr = "xmppserver";

	private static String gtalkPwd = "thaiha";

	private static XMPPConnection con;

	private static Hashtable threadIds = new Hashtable();

	public static void main(String[] args) throws XMPPException {
		final String host = args.length > 1 ? args[0] : "localhost";
		final String port = args.length > 1 ? args[1] : (args.length == 1 ? args[0] : "5900");
		
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
							if (!threadIds.containsKey(msg.getThread())) {
								threadIds.put(msg.getThread(), "true");
								/* a message of a brand new thread */
								Chat chat = new Chat(con, "xmppclient@gmail.com", msg.getThread());
								Socket socket = new Socket(host, Integer.parseInt(port));
								System.out.println("Chat not exist, create new tunnel " + msg.getThread());
								new XMPPOverTCPThread(socket, chat).start();
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
