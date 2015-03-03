package com.polarbirds.screencapture.plugin.uploader;

import com.jcraft.jsch.*;
import com.polarbirds.screencapture.plugin.Manifest;
import com.polarbirds.screencapture.plugin.PluginInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Uploader implements PluginInterface {

    private final String configurationFile = "uploader.txt";

    private Session session;
    private Channel channel;
    private ChannelSftp sftp;
    private JSch ssh;
    private Configuration conf;
    private Manifest manifest;

    public Uploader() throws IOException, JSchException{

        String author = "Trond Humborstad";
        String name = "Uploader";
        String description = "A plugin which uploads the captured image to a SFTP server";
        double version = 1.0;
        manifest = new Manifest(author, name, description, version);

        conf = new Configuration(configurationFile);

        ssh = new JSch();
        JSch.setConfig("StrictHostKeyChecking", "no");

        session = ssh.getSession(conf.getUser(), conf.getServer(), conf.getPort());

        if(conf.getPrivateKey() != null)
            ssh.addIdentity(conf.getPrivateKey());
        else
            session.setPassword(conf.getPassword());

    }
    
    @Override
    public Manifest manifest() {
        return manifest;
    }

    @Override
    public void run(BufferedImage img) {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            connect();
            String filename = getValidFileName();
            sftp.put(is, conf.getDirectory() + filename, ChannelSftp.OVERWRITE);
            sftp.exit();
            disconnect();

            setClipboard(conf.getHTTPServer() + filename);
            log(conf.getHTTPServer() + filename);

        } catch (JSchException | SftpException e){
            System.err.println("Something related to the transfer went bad. " + e.getMessage());
        } catch (IOException e){
            System.err.println("Something related to IO went bad. " + e.getMessage());
        }
    }
    
    private void connect() throws JSchException{
        session.connect();
        channel = session.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
    }
    
    private void disconnect(){
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
    
    public boolean fileExist(String file){
        //TODO: This isn't good, as catching is expensive for often-performed tasks.
        try {
            sftp.ls(file);
            return true;
        } catch (SftpException e) {
            return false;
        }
    }
    
    private String getValidFileName(){
        String out = "";
        do{
            out = randomString() + ".png";
        } while(fileExist(conf.getDirectory() + out));
        return out;
    }
    
    public String randomString(){
        Random r = new Random();

        String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String randomstring = "";
        for (int i = 0; i < 5; i++) {
            randomstring += alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return randomstring;
    }
    
    public void setClipboard(String myString){
        StringSelection stringSelection = new StringSelection (myString);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard ();
        clpbrd.setContents(stringSelection, null);
        if(System.getProperty("os.name").equals("Linux")){
            JOptionPane.showMessageDialog(null,
                    "<html>Your URL is: " + myString
                    +"<br/>This has already been copied to your clipboard.<br/><br/>"
                    +"<br/>Due to limitations in Linux, your URL will only be in your clipboard<br/>"
                    +"while this window is open.<br/>Press OK to close</html>"
            );
        }
    }
    
    public void log(String arg) throws IOException{
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(conf.getLogfile(), true)));
        out.println(arg);
        out.close();
    }

}
