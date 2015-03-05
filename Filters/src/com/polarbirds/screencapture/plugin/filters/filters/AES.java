package com.polarbirds.screencapture.plugin.filters.filters;

import com.polarbirds.screencapture.plugin.filters.FilterInterface;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Tsh on 3/4/2015.
 */
public class AES implements FilterInterface {

    /*
    And this, kids, is why you don't use AES ECB.
     */

    @Override
    public void manipulate(BufferedImage img) {
        try{
            byte[] keyBytes = new byte[] {

                    (byte) 0xff,(byte) 0xe3,(byte) 0xa2,(byte) 0xc3,
                    (byte) 0x4e,(byte) 0x8e,(byte) 0xf6,(byte) 0x0c,
                    (byte) 0xd6,(byte) 0x5c,(byte) 0xfa,(byte) 0x0b,
                    (byte) 0xee,(byte) 0x0d,(byte) 0xae,(byte) 0xef,
                    (byte) 0xae,(byte) 0x61,(byte) 0xd2,(byte) 0xe3,
                    (byte) 0x13,(byte) 0x37,(byte) 0x13,(byte) 0x37

            };

            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {

                    int rgba = img.getRGB(x, y);
                    byte[] out = cipher.doFinal(intToByteArray(rgba));
                    img.setRGB(x, y, byteArrayToInt(out));

                }
            }

        } catch (IllegalBlockSizeException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.err.println("Something related to encryption went wrong: " + e.getMessage());
        }

    }

    private byte[] intToByteArray(int integer){
        byte[] out = new byte[4];

        out[0] = (byte) (integer >> 24);
        out[1] = (byte) (integer >> 16);
        out[2] = (byte) (integer >> 8);
        out[3] = (byte) integer;

        return out;
    }

    private int byteArrayToInt(byte[] bytes){
        return (bytes[0] << 24) & 0xff000000 |
                (bytes[1] << 16) & 0x00ff0000 |
                (bytes[2] << 8) & 0x0000ff00 |
                (bytes[3]) & 0x000000ff;
    }
}
