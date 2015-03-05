package com.polarbirds.screencapture.plugin.filters.filters;

import com.polarbirds.screencapture.plugin.filters.FilterInterface;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Tsh on 3/5/2015.
 */
public class Greyscale implements FilterInterface {
    @Override
    public void manipulate(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgba = img.getRGB(x, y);
                Color col = new Color(rgba, true);

                int average = (col.getRed() + col.getGreen() + col.getBlue()) / 3;
                col = new Color(average, average, average);

                img.setRGB(x, y, col.getRGB());
            }
        }
    }
}
