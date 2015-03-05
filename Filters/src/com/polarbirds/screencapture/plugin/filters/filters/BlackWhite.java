package com.polarbirds.screencapture.plugin.filters.filters;

import com.polarbirds.screencapture.plugin.filters.FilterInterface;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Tsh on 3/4/2015.
 */
public class BlackWhite implements FilterInterface {

    @Override
    public void manipulate(BufferedImage img) {
        // You might want to adjust this value
        final int MONO_THRESHOLD = 308;

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgba = img.getRGB(x, y);
                Color col = new Color(rgba, true);

                if (col.getRed() + col.getGreen() + col.getBlue() > MONO_THRESHOLD)
                    col = new Color(255, 255, 255);
                else
                    col = new Color(0, 0, 0);

                img.setRGB(x, y, col.getRGB());
            }
        }
    }
}
