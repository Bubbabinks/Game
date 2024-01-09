package main;

import game.Render;

import java.awt.*;
import java.awt.image.BufferedImage;

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

}
