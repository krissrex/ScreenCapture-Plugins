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
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Random;

public class Uploader implements PluginInterface {

    private Session session;
    private Channel channel;
    private ChannelSftp sftp;
    private JSch ssh;
    private Map<String, String> conf;
    private Manifest manifest;

    public Uploader() {


        String author = "Trond Humborstad";
        String name = "Uploader";
        String description = "A plugin which uploads the captured image to a SFTP server";
        double version = 1.0;
        manifest = new Manifest(author, name, description, version);

    }

    @Override
    public void setConfiguration(Object configuration) {
        this.conf = (Map<String, String>) configuration;
    }
    
    @Override
    public Manifest manifest() {
        return manifest;
    }

    @Override
    public void run(BufferedImage img) {
        try {

            setup();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            connect();
            String filename = getValidFileName();
            sftp.put(is, conf.get("directory") + filename, ChannelSftp.OVERWRITE);
            sftp.exit();
            disconnect();

            String remotePath = conf.get("httpServer") + filename;
            setClipboard(remotePath);

        } catch (JSchException | SftpException e){
            System.err.println("Something related to the transfer went bad. " + e.getMessage());
        } catch (IOException e){
            System.err.println("Something related to IO went bad. " + e.getMessage());
        } catch (URISyntaxException e) {
            System.err.println("Something related to the URI-formatting went bad. " + e.getMessage());
        }
    }

    private void setup() throws IOException, JSchException, URISyntaxException{
        ssh = new JSch();
        JSch.setConfig("StrictHostKeyChecking", "no");

        session = ssh.getSession(conf.get("user"), conf.get("server"), Integer.valueOf(conf.get("port")));

        if(conf.get("privateKey") != null)
            ssh.addIdentity(conf.get("privateKey"));
        else
            session.setPassword(conf.get("password"));
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
        int length = Integer.parseInt(conf.getOrDefault("length", "5"));
        do{
            out = randomString(length) + ".png";
        } while(fileExist(conf.get("directory") + out));
        return out;
    }
    
    public String randomString(int length){
        Random r = new Random();

        String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String randomstring = "";
        for (int i = 0; i < length; i++) {
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
}
