package com.polarbirds.screencapture.plugin.datestamp;

import com.polarbirds.screencapture.plugin.Manifest;
import com.polarbirds.screencapture.plugin.PluginInterface;

import java.awt.image.BufferedImage;

/**
 * Created by kristian on 03.03.15.
 */
public class DateStamp implements PluginInterface {

    private static final Manifest manifest = new Manifest("Kristian Rekstad", "Date stamp", "Applies a date stamp to the image", 1.0);

    @Override
    public Manifest manifest() {
        return manifest;
    }

    @Override
    public void run(BufferedImage img) {

    }
}
