package org.javavnc.tcp2xmpp;

import java.net.Socket;
import java.util.Date;

import org.apache.log4j.Logger;
import org.javavnc.common.XMPPConnectionManager;
import org.javavnc.common.TCP2XMPPPumpThread;
import org.javavnc.common.XMPP2TCPPumpThread;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class TCPOverXMPPThread extends Thread {

    private static Logger logger = Logger.getLogger(TCPOverXMPPThread.class);

    private Socket socket;
    private String resource;
    private XMPPConnection connection;
    private Chat chat;

    public TCPOverXMPPThread(Socket socket, String gtalkUsr, String gtalkPwd, String recipientId) throws XMPPException {
        super("TCPOverXMPPThread");
        this.socket = socket;
        XMPPConnection con = new XMPPConnectionManager().getGoogleConnection();
        this.resource = "TCPOverXMPPThread_" + (new Date()).getTime();
        con.login(gtalkUsr, gtalkPwd, resource);
        this.connection = con;
        chat = connection.getChatManager().createChat(recipientId, null);
    }

    public void run() {
        try {
            Thread t1 = new TCP2XMPPPumpThread(socket, chat);
            Thread t2 = new XMPP2TCPPumpThread(chat, socket);
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            socket.close();
            logger.info("Closed socket.");
            connection.disconnect();
            logger.info("Closed XMPPConnection.");
        } catch (Exception e) {
            logger.error("Error when establishing tcp/xmpp tunnel.", e);
        }
    }
}