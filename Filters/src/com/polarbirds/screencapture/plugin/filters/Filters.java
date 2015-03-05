package com.polarbirds.screencapture.plugin.filters;

import com.polarbirds.screencapture.plugin.Manifest;
import com.polarbirds.screencapture.plugin.PluginInterface;
import com.polarbirds.screencapture.plugin.filters.filters.AES;

import java.awt.image.BufferedImage;

/**
 * Created by Tsh on 3/4/2015.
 */
public class Filters implements PluginInterface {

    private Manifest manifest = new Manifest("Trond Humborstad", "Filters", "Applies filters.", 1.0);

    @Override
    public Manifest manifest() {
        return manifest;
    }

    @Override
    public void run(BufferedImage img) {
        new AES().manipulate(img);
        //new Grayscale().manipulate(img);
    }
}
