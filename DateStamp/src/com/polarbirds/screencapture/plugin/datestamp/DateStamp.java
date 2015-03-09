package com.polarbirds.screencapture.plugin.datestamp;

import com.polarbirds.screencapture.plugin.Manifest;
import com.polarbirds.screencapture.plugin.PluginInterface;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by kristian on 03.03.15.
 */
public class DateStamp implements PluginInterface {

    private static final Manifest manifest = new Manifest("Kristian Rekstad", "Date stamp", "Applies a date stamp to the image", 1.0);

    @Override
    public void setConfiguration(Object configuration) {

    }

    @Override
    public Manifest manifest() {
        return manifest;
    }

    @Override
    public void run(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        paint(g, img.getWidth(), img.getHeight());
        g.dispose();
    }

    protected void paint(Graphics2D g, int width, int height){
        int x = 10;
        int y = height-10;

        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

        Font f = new Font("Arial", Font.PLAIN, 12);

        g.setFont(f);
        g.setColor(Color.red);
        g.drawString(date, x, y);
    }
}
