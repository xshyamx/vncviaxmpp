package org.javavnc.xmpp2tcp;

import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;

import org.javavnc.common.TCP2XMPPPumpThread;
import org.javavnc.common.XMPP2TCPPumpThread;

public class XMPPOverTCPThread extends Thread {
    private static Logger logger = Logger.getLogger(XMPPOverTCPThread.class);
    private Socket socket = null;
    private Chat chat;
    private List<Object> errors;

    public XMPPOverTCPThread(Socket socket, Chat chat, List<Object> errors) {
        super("XMPPOverTCPThread");
        this.socket = socket;
        this.chat = chat;
        this.errors = errors;
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
        } catch (Exception e) {
            logger.error("Error when establishing xmpp/tcp tunnel.", e);
            errors.add(e);
        }
    }
}
