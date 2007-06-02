package com.fcg.xmpptcp.common;

import org.jivesoftware.smack.sasl.SASLPlainMechanism;
import org.jivesoftware.smack.SASLAuthentication;


public class MySASLPlainMechanism extends SASLPlainMechanism {

    public MySASLPlainMechanism(SASLAuthentication saslAuthentication) {
        super(saslAuthentication);
    }

    protected String getAuthenticationText(String username, String host, String password) {
        if ("talk.google.com".equalsIgnoreCase(host)) {
            host = "gmail.com";
        }
        return super.getAuthenticationText(username, host, password);
    }
}
