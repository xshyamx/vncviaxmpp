package org.javavnc.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class VNCManager {

    private static Logger logger = Logger.getLogger(VNCManager.class);

    private void unzip(ZipInputStream zis, File destDir) throws Exception {
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                String destFN = destDir.getAbsolutePath() + File.separator + entry.getName();
                createDirectories(destFN);
                FileOutputStream fos = new FileOutputStream(destFN);
                IOUtils.copy(zis, fos);
                fos.close();
            }
        }
    }

    private void createDirectories(String filePath) {
        File file = new File(filePath);

        // List all directories to be created
        File directory = file.getParentFile();
        List<File> directories = new ArrayList<File>();
        while (directory != null && !directory.exists()) {
            directories.add(directory);
            directory = directory.getParentFile();
        }

        // Create directories (in reverse order)
        Collections.reverse(directories);
        for (File dir : directories) {
            dir.mkdir();
        }
    }

    private File installVNC() throws Exception {

        // create a temp folder
        File tempFile = File.createTempFile("tightvnc", ".tmp");
        tempFile.delete();
        File tempDir = new File(tempFile.getAbsolutePath());
        tempDir.mkdir();

        // unzip /tightvnc-1.3.9_x86.zip to that folder
        InputStream is = VNCManager.class.getResourceAsStream("/tightvnc-1.3.9_x86.zip");
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
        unzip(zis, tempDir);
        zis.close();

        // find vnc base dir
        File baseDir = findFile(tempDir, "vncviewer.exe").getParentFile();

        // copy vnc.reg to that folder
        is = VNCManager.class.getResourceAsStream("/vnc.reg");
        FileOutputStream os = new FileOutputStream(new File(baseDir, "vnc.reg"));
        IOUtils.copy(is, os);
        is.close();
        os.close();

        String cmdLine = "REGEDIT /S \"" + baseDir.getAbsolutePath() + "/vnc.reg\"";
        logger.info("Executing command: " + cmdLine);
        Process p = Runtime.getRuntime().exec(new String[]{"REGEDIT", "/S", baseDir.getAbsolutePath() + "/vnc.reg"});
        p.waitFor();

        return baseDir;
    }

    private File findFile(File dir, String fileName) {
        File result = null;
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            File child = new File(dir, children[i]);
            if (child.isFile() && child.getName().equalsIgnoreCase(fileName)) {
                result = child;
                break;
            } else if (child.isDirectory()) {
                File file = findFile(child, fileName);
                if (file != null) {
                    result = file;
                    break;
                }
            }
        }
        return result;
    }

    public Process startVNCServer() throws Exception {
        File vncBaseDir = installVNC();

        String winVNC = vncBaseDir.getAbsolutePath() + "/WinVNC.exe";
        logger.info("Executing command: " + winVNC + " -kill");
        Runtime.getRuntime().exec(new String[] { winVNC, "-kill" }).waitFor();

        logger.info("Executing command: " + winVNC);
        Process p = Runtime.getRuntime().exec(winVNC);
        return p;
    }

    public Process startVNCClient(String serverHost, int serverPort) throws Exception {
        File vncBaseDir = installVNC();

        String vncViewer = vncBaseDir.getAbsolutePath() + "/vncviewer.exe";
        String hostPortParam = serverHost + "::" + serverPort;
        logger.info("Executing command: " + vncViewer + " " + hostPortParam);
        Process p = Runtime.getRuntime().exec(new String[] { vncViewer, hostPortParam });
        return p;
    }
}
