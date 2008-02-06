package org.javavnc.xmpp2tcp;

import java.net.Socket;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class MyPacketListener implements PacketListener {

    private static Logger logger = Logger.getLogger(MyPacketListener.class);

    private Map<String, Thread> threads;
    private String host;
    private String sender;
    private XMPPConnection con;
    private List<Object> errors;

    public MyPacketListener(String host, String sender, XMPPConnection con, List<Object> errors) {
        this.threads = Collections.synchronizedMap(new Hashtable<String, Thread>());
        this.host = host;
        this.sender = sender;
        this.con = con;
        this.errors = errors;
    }

    public void processPacket(Packet packet) {
        try {
            if (packet instanceof Message) {
                Message msg = (Message) packet;
                String thread = msg.getThread();
                boolean foundThread = false;
                if (threads.containsKey(thread)) {
                    Thread t = threads.get(thread);
                    if (t.isAlive()) {
                        foundThread = true;
                    }
                }
                if (!foundThread) {
                    Socket socket;
                    try {
                        socket = new Socket(host, 5900);
                        /* a message of a brand new thread */
                        Chat chat = con.getChatManager().createChat(sender, msg.getThread(), null);
                        logger.info("Chat not exist, create new tunnel " + msg.getThread());
                        Thread t = new XMPPOverTCPThread(socket, chat, errors);
                        t.start();
                        threads.put(thread, t);
                    } catch (Exception e) {
                        /* cannot open a socket? inform the sender */
                        Chat chat = con.getChatManager().createChat(sender, msg.getThread(), null);
                        chat.sendMessage("0:_end");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception when processing XMPP message.", e);
            errors.add(e);
        }
    }
}