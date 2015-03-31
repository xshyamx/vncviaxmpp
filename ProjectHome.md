This project creates an XMPP tunnel to allow a TCP connection can be made through XMPP network (e.g. Google Talk). With this tunnel, a remote program can send data to a server. For example, we can use VNC to control a machine without a direct connection to that server. This will be useful in a coporate environment where firewalls/proxies are installed.

Model: TCP client program --(TCP connection)->TCP2XMPP-----(XMPP network)-->XMPP2TCP---(TCP connection)--->TCP server program.
