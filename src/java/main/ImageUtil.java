package main;

import game.Render;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.Arrays;

public class ImageUtil {

    public BufferedImage image;
    public int centerX, centerY, width, height;

    private int subWidth, subHeight;

    public ImageUtil(Image image) {
        this.image = (BufferedImage) image;
        width = this.image.getWidth();
        height = this.image.getHeight();
        centerX = Render.hsw - width/2;
        centerY = Render.hsh - height/2;
    }

    public void setSubImageWidth(int subWidth) {
        this.subWidth = subWidth;
    }

    public void setSubImageHeight(int subHeight) {
        this.subHeight = subHeight;
    }

    public void setSubDimension(Dimension dimension) {
        subWidth = dimension.width;
        subHeight = dimension.height;
    }

    public Image getSubImage(int x, int y) {
        return image.getSubimage(x*subWidth, y*subHeight, subWidth, subHeight);
    }

    public static boolean imagesEqual(Image image1, Image image2) {
        try {

            PixelGrabber grab1 =new PixelGrabber(image1, 0, 0, -1, -1, false);
            PixelGrabber grab2 =new PixelGrabber(image2, 0, 0, -1, -1, false);

            int[] data1 = null;

            if (grab1.grabPixels()) {
                int width = grab1.getWidth();
                int height = grab1.getHeight();
                data1 = new int[width * height];
                data1 = (int[]) grab1.getPixels();
            }

            int[] data2 = null;

            if (grab2.grabPixels()) {
                int width = grab2.getWidth();
                int height = grab2.getHeight();
                data2 = new int[width * height];
                data2 = (int[]) grab2.getPixels();
            }

            return Arrays.equals(data1, data2);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
