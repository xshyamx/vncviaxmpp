package com.fcg.xmpptcp.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class VNCUtils {
	
	private static File getVNCHome() {
		URL source = VNCUtils.class.getProtectionDomain().getCodeSource().getLocation();
		File file = new File(source.getFile());
		File vncHome = new File(file.getParentFile().getAbsolutePath() + "/tightvnc-1.3.8_x86");
		return vncHome;
	}
	
	public static void installVNCServer() throws IOException, InterruptedException {
		String cmdLine = "REGEDIT /S \"" + getVNCHome() + "/vnc.reg\"";
		System.out.println(cmdLine);
		Process p = Runtime.getRuntime().exec(cmdLine);
		p.waitFor();
	}
	
	public static Process startVNCServer() throws IOException, InterruptedException {
		String cmdLine = "\"" + getVNCHome() + "/WinVNC.exe\"";
		System.out.println(cmdLine);
		Process p = Runtime.getRuntime().exec(cmdLine);
		p.waitFor();
		return p;
	}
	
	public static void stopVNCServer(Process p) {
		// stop process
		p.destroy();
	}

	public static void removeVNCServer() {
		//TODO: remove registry		
	}
	
	public static void startVNCClient(String serverHost, int serverPort) throws IOException {
		String cmdLine = "\"" + getVNCHome() + "/vncviewer.exe\" " + serverHost + "::" + serverPort;
		System.out.println(cmdLine);
		Runtime.getRuntime().exec(cmdLine);
	}
}
