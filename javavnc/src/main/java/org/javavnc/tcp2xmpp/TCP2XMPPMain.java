package org.javavnc.tcp2xmpp;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.log4j.Logger;
import org.javavnc.common.VNCManager;

public class TCP2XMPPMain {

    private static Logger logger = Logger.getLogger(TCP2XMPPMain.class);

    /**
     * http://java.sun.com/docs/books/tutorial/networking/sockets/clientServer.html
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = null;

        String port = null;
        String username = null;
        String password = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            try {
                int p = Integer.parseInt(arg);
                port = String.valueOf(p);
            } catch (Exception e) {
                if (username == null) {
                    username = arg;
                    if (username.indexOf('@') >= 0) {
                        username = username.substring(0, username.indexOf('@'));
                    }
                } else {
                    password = arg;
                }
            }
        }
        if (port == null)
            port = "5900";
        if (username == null)
            username = "xmppclient";
        if (password == null)
            password = "thaiha";

        logger.info("Listen port:" + port + "; username: " + username + "; password:" + password);

        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
        } catch (IOException e) {
            logger.error("Could not listen on port: " + port);
            System.exit(-1);
        }

        // start vnc client
        VNCManager manager = new VNCManager();
        manager.startVNCClient("localhost", Integer.parseInt(port));

        // usr/pwd: xmppclient@gmail.com/thaiha
        // usr/pwd: xmppserver@gmail.com/thaiha
        Thread t = new TCPOverXMPPThread(serverSocket.accept(), username, password, "xmppserver@gmail.com");
        t.start();

        while (t.isAlive()) {
            Thread.sleep(1000);
        }

        serverSocket.close();
    }
}