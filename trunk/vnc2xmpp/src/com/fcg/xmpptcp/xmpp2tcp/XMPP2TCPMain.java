package com.fcg.xmpptcp.xmpp2tcp;

import java.net.Socket;
import java.util.Hashtable;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import com.fcg.xmpptcp.common.ConnectionUtil;


public class XMPP2TCPMain {
	private static String gtalkUsr = "xmppserver";

	private static String gtalkPwd = "thaiha";

	private static XMPPConnection con;

	private static Hashtable threadIds = new Hashtable();

	public static void main(String[] args) throws XMPPException {
		final String host = args.length > 1 ? args[0] : "localhost";
		final String port = args.length > 1 ? args[1] : (args.length == 1 ? args[0] : "5900");
		
		// If we remove this, the main thread will exit and the background threads exit too --> server stops immediately
		XMPPConnection.DEBUG_ENABLED = true;

		con = ConnectionUtil.getGoogleConnection();//new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
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

		// Verify this code to make sure we can relaunch this server when error happens
		/*
		// Register connection listener
		ConnectionListener conListener = new ConnectionListener() {

			public void connectionClosed() {
			}

			public void connectionClosedOnError(Exception e) {
				try {
					threadIds.clear();
					con.close();
				} catch (Exception e2) {
				}
				try {
					wait(10000);
				} catch (Exception e3) {
				}
				Thread t = new Thread() {
					public void run() {
						try {
							XMPP2TCPMain.main(XMPP2TCPMain.args);
						} catch (Exception e) {
						}
					}
				};
				t.start();
			}
		};
		con.addConnectionListener(conListener);
		*/
	}
}
