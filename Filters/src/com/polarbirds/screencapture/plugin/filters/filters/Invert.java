package com.polarbirds.screencapture.plugin.filters.filters;

import com.polarbirds.screencapture.plugin.filters.FilterInterface;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Tsh on 3/4/2015.
 */
public class Invert implements FilterInterface {
    @Override
    public void manipulate(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgba = img.getRGB(x, y);
                Color col = new Color(rgba, true);

                col = new Color(
                        255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue()
                );

                img.setRGB(x, y, col.getRGB());
            }
        }
    }
}
