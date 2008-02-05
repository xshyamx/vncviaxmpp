// vncclient.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "talk/base/thread.h"
#include "talk/base/ssladapter.h"
#include "talk/base/physicalsocketserver.h"
#include "talk/examples/login/xmpppump.h"
#include "talk/examples/login/xmppsocket.h"


int _tmain(int argc, _TCHAR* argv[])
{
	// Initialize SSL channel.
	talk_base::InitializeSSL();

	// Create the signaling thread.
	// AutoThread captures the current OS thread and sets it to be
	// ThreadManager::CurrentThread, which will be called and used by SessionManager 
	talk_base::PhysicalSocketServer ss;
	talk_base::AutoThread main_thread(&ss);

	// Get the information we'll need to sign in.
	XmppPump pump;
	buzz::Jid jid;
	buzz::XmppClientSettings xcs;
	talk_base::InsecureCryptStringImpl pass;

	jid = buzz::Jid("hathanhthai2@gmail.com");
	pass.password() = "purplecat809";

	xcs.set_user(jid.node());
	xcs.set_resource("pcp");  // Arbitrary resource name.
	xcs.set_host(jid.domain());
	xcs.set_use_tls(true);
	xcs.set_pass(talk_base::CryptString(pass));
	xcs.set_server(talk_base::SocketAddress("talk.google.com", 5222));

	// Sign up to receive signals from the XMPP pump to track sign in progress.
	//pump.client()->SignalStateChange.connect(&fs_client, &FileShareClient::OnStateChange);

	// Queue up the sign in request.
	pump.DoLogin(xcs, new XmppSocket(true), NULL);

	// Start the thread and run indefinitely.
	main_thread.Run();


	return 0;
}

