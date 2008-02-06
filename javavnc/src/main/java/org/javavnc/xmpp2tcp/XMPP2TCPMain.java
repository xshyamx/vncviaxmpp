package org.javavnc.xmpp2tcp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.javavnc.common.VNCManager;
import org.javavnc.common.XMPPConnectionManager;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;

public class XMPP2TCPMain {

    private static Logger logger = Logger.getLogger(XMPP2TCPMain.class);

    public static void main(String[] args) throws Exception {

        // Start VNC server
        VNCManager manager = new VNCManager();
        manager.startVNCServer();

        // Create an XMPP connection to service VNC connection requests
        String host = "localhost";
        String sender = "xmppclient@gmail.com";
        maintainXMPPConnection(host, sender);
    }

    private static void maintainXMPPConnection(String host, String sender) throws Exception {
        do {
            logger.info("Trying to setup XMPPConnection ...");
            XMPPConnection  con = null;
            List<Object> errors = Collections.synchronizedList(new ArrayList<Object>());
            try {
                XMPPConnectionManager manager = new XMPPConnectionManager();
                con = manager.getGoogleConnection();
                String gtalkUsr = "xmppserver";
                String gtalkPwd = "thaiha";
                con.login(gtalkUsr, gtalkPwd);
                PacketFilter filter = new AndFilter(new PacketTypeFilter(Message.class), new FromContainsFilter(sender));
                PacketListener myListener = new MyPacketListener(host, sender, con, errors);
                con.addPacketListener(myListener, filter);
                ConnectionListener myConnectionListener = new MyConnectionListener(errors);
                con.addConnectionListener(myConnectionListener);
            } catch (Exception e) {
                logger.error("Could not setup XMPPConnection.", e);
                errors.add(e);
            }

            // Sleep until the XMPP connection has some problem then wake up...
            if (con != null) {
                do {
                    Thread.sleep(1000);
                } while (errors.isEmpty());
            }

            // destroy connection
            if (con != null) {
                con.disconnect();
            }

            // wait for 3s and try again
            Thread.sleep(3000);

        } while (true);
    }
}
