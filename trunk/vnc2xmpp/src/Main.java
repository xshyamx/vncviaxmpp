import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;



public class Main {

	/**
	 * http://java.sun.com/docs/books/tutorial/networking/sockets/clientServer.html
	 * 
	 * @param args
	 * @throws XMPPException 
	 */
	public static void main(String[] args) throws XMPPException {
		XMPPConnection.DEBUG_ENABLED = true;

		XMPPConnection connection = new SSLXMPPConnection("talk.google.com", 443, "gmail.com");
		connection.login("hathanhthai", "purplecat809");
		Chat chat = connection.createChat("hathanhthai2@gmail.com");
		chat.sendMessage("Howdy2!");
		connection.close();
		 
	}

} 
