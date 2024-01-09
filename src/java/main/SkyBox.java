package main;

import java.awt.*;

public enum SkyBox {

    day;

    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
