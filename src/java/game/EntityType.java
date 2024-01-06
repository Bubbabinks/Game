package game;

import main.FileManager;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public enum EntityType implements Serializable {

    player(FileManager.getEntityTypeImage(0, 0));


    private BufferedImage image;

    private EntityType(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

}
