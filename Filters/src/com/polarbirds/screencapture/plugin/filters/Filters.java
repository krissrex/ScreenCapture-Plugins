package com.polarbirds.screencapture.plugin.filters;

import com.polarbirds.screencapture.plugin.Manifest;
import com.polarbirds.screencapture.plugin.PluginInterface;
import com.polarbirds.screencapture.plugin.filters.filters.AES;
import com.polarbirds.screencapture.plugin.filters.filters.BlackWhite;
import com.polarbirds.screencapture.plugin.filters.filters.Greyscale;
import com.polarbirds.screencapture.plugin.filters.filters.Invert;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tsh on 3/4/2015.
 */
public class Filters implements PluginInterface {

    private Manifest manifest = new Manifest("Trond Humborstad", "Filters", "Applies filters.", 1.0);
    private Map<String, FilterInterface> filters;
    private ArrayList<String> filtersToApply;

    public Filters(){
        // I'm afraid this is a bad hack.
        filters = new HashMap<>();
        filters.put("AES", new AES());
        filters.put("BlackWhite", new BlackWhite());
        filters.put("Greyscale", new Greyscale());
        filters.put("Invert", new Invert());
    }

    @Override
    public void setConfiguration(Object configuration) {
        filtersToApply = (ArrayList<String>) configuration;
    }

    @Override
    public Manifest manifest() {
        return manifest;
    }

    @Override
    public void run(BufferedImage img) {
        for(String i : filtersToApply){
            FilterInterface filter = filters.get(i);
            if(filter != null) {
                filter.manipulate(img);
            }
        }
    }
}
