import com.polarbirds.screencapture.plugin.Manifest;
import com.polarbirds.screencapture.plugin.PluginInterface;

import java.awt.image.BufferedImage;

/**
 * Created by kristian on 02.03.15.
 */
public class SamplePlugin implements PluginInterface {

    private Manifest manifest = new Manifest("Kristian Rekstad", "SamplePlugin", "It does nothing!", 1.0);

    @Override
    public Manifest manifest() {
        return manifest;
    }

    @Override
    public void run(BufferedImage img) {
        return;
    }
}
