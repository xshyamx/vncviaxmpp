package com.fcg.xmpptcp.xmpp2tcp;

import java.net.Socket;
import java.util.Hashtable;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionListener;
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
import com.fcg.xmpptcp.common.VNCUtils;


public class XMPP2TCPMain {
	private static String gtalkUsr = "xmppserver";

	private static String gtalkPwd = "thaiha";

	private static XMPPConnection con;

	private static Hashtable threadIds = new Hashtable();

	private static String[] args;

	public static void main(String[] args) throws XMPPException{
		/* save this value to be used when relaunching server */
		XMPP2TCPMain.args = args;

		String tmphost = null;
		String tmpport = null;
		String tmpsender = null;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.indexOf('@') >= 0) {
				tmpsender = arg;
			} else {
				// check if this is a number
				try {
					int p = Integer.parseInt(arg);
					tmpport = String.valueOf(p);
				} catch (Exception e) {
					tmphost = arg;
				}
			}
		}
		final String host = tmphost != null ? tmphost : "localhost";
		final String port = tmpport != null ? tmpport : "5900";
		final String sender = tmpsender != null ? tmpsender : "xmppclient@gmail.com";
		
		System.out.println("Host:" + host + "; port:" + port + "; sender:" + sender);
		
		// If we remove this, the main thread will exit and the background threads exit too --> server stops immediately
		XMPPConnection.DEBUG_ENABLED = true;

		/* try to get the connection, if failed, wait 1s and try again until we success */
		long waitTime = 1000;
		do {
			con = ConnectionUtil.getGoogleConnection();//new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
			if (con == null) {
				try {
					Thread.sleep(waitTime);
					waitTime *= 2; // next time, wait a longer time
				} catch (Exception e) {
				}
			}
		} while (con == null);

		con.login(gtalkUsr, gtalkPwd);

		// Accept only messages from client
		PacketFilter filter = new AndFilter(
				new PacketTypeFilter(Message.class), new FromContainsFilter(
						sender));

		PacketListener myListener = new PacketListener() {
			public void processPacket(Packet packet) {
				try {
					if (packet instanceof Message) {
						Message msg = (Message) packet;
						boolean existConnection;
						synchronized(threadIds) {
							if (!threadIds.containsKey(msg.getThread())) {
								existConnection = false;
								threadIds.put(msg.getThread(), "true");
							} else {
								existConnection = true;
							}
						}
						if (!existConnection) {
							Socket socket;
							try {
								socket = new Socket(host, Integer.parseInt(port));
								/* a message of a brand new thread */
								Chat chat = new Chat(con, sender, msg.getThread());
								System.out.println("Chat not exist, create new tunnel " + msg.getThread());
								new XMPPOverTCPThread(socket, chat).start();
							} catch (Exception e) {
								/* cannot open a socket? inform the sender*/
								new Chat(con, sender, msg.getThread()).sendMessage("0:_end");
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
		// Register connection listener
		con.addConnectionListener(new ConnectionListener() {

			public void connectionClosed() {
			}

			public void connectionClosedOnError(Exception e) {
				/* cleanup */
				try {
					System.out.println("RESTARTING....");
					threadIds.clear();
					con.close();
				} catch (Exception e2) {
				}

				/* sleep 10s because the network may not be stable soon */
				try {
					Thread.sleep(10000);
				} catch (Exception e2) {
				}

				/* relaunch the main flow */
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
		});
		
		// Start VNC server
		if (Integer.parseInt(port) == 5900) {
			try {
				VNCUtils.installVNCServer();
				VNCUtils.startVNCServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
