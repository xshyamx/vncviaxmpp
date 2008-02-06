package org.javavnc.xmpp2tcp;

import java.util.List;

import org.jivesoftware.smack.ConnectionListener;

public class MyConnectionListener implements ConnectionListener {
    private List<Object> errors;

    public MyConnectionListener(List<Object> errors) {
        this.errors = errors;
    }

    public void connectionClosed() {
        errors.add("connectionClosed.");
    }

    public void connectionClosedOnError(Exception e) {
        errors.add("connectionClosedOnError.");
    }

    public void reconnectingIn(int seconds) {
        // Ignore
    }

    public void reconnectionFailed(Exception e) {
        errors.add("reconnectionFailed.");
    }

    public void reconnectionSuccessful() {
        // Ignore
    }

}
