package org.javavnc.common;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPConnectionManager {

    private static Logger logger = Logger.getLogger(XMPPConnectionManager.class);

    private boolean isPortOpen(String host, int port) {
        boolean open;
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(host, port), 5000);
            open = true;
            sock.close();
        } catch (Exception e) {
            logger.info("Could not open port " + port + " on host " + host, e);
            open = false;
        }
        return open;
    }

    public XMPPConnection getXMPPConnection(String host, int port) throws XMPPException {

        XMPPConnection con = null;

        if (host.equalsIgnoreCase("talk.google.com")) {
            if (isPortOpen("talk.google.com", 5222)) {
                ConnectionConfiguration configuration = new ConnectionConfiguration("talk.google.com", 5222,
                        "gmail.com");
                con = new XMPPConnection(configuration);
            } else if (isPortOpen("talk.google.com", 443)) {
                con = new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
            }
        }

        if (con == null) {
            ConnectionConfiguration configuration = new ConnectionConfiguration(host, port);
            con = new XMPPConnection(configuration);
        }
        con.connect();
        return con;
    }

    public XMPPConnection getGoogleConnection() throws XMPPException {
        return getXMPPConnection("talk.google.com", 5222);
    }
}
