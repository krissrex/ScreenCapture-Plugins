package com.polarbirds.screencapture.plugin.uploader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * Created by Tsh on 2/28/2015.
 */
public class Configuration {

    private Properties prop;

    private String logfile;
    private String directory;
    private String server;
    private int port;
    private String user;
    private String password;
    private String privateKey;
    private String httpServer;

    public Configuration(String configFile) throws IOException{
        prop = new Properties();
        load(configFile);
    }

    private void load(String configFile) throws IOException{

        /*
        The following lines does some modifications to what's stored in the config-file.
        Things like path-handling etc.
         */
        String data = new String(Files.readAllBytes(new File(configFile).toPath()));

        //Windows-paths needs to be escaped properly.
        data = data.replace("\\", "\\\\");

        //Turn it into a stream and feed it to prop.load()
        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());

        prop.load(bais);
        logfile = prop.getProperty("logfile", "out.txt");
        directory = prop.getProperty("directory");
        server = prop.getProperty("server");
        port = Integer.parseInt(prop.getProperty("port", "22"));
        user = prop.getProperty("user");
        password = prop.getProperty("password");
        privateKey = prop.getProperty("privateKey", null);
        httpServer = prop.getProperty("httpServer");

    }

    public String getLogfile() {
        return logfile;
    }

    public String getHTTPServer() {
        return httpServer;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }

    public int getPort() {
        return port;
    }

    public String getServer() {
        return server;
    }

    public String getDirectory() {
        return directory;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
