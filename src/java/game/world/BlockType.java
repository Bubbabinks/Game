package game.world;

import main.FileManager;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public enum BlockType implements Serializable {

    stone(FileManager.getBlockTypeImage(0,0), true),
    dirt(FileManager.getBlockTypeImage(0, 1), true),
    grass(FileManager.getBlockTypeImage(0, 2), true),
    log(FileManager.getBlockTypeImage(0, 3), false),
    leaves(FileManager.getBlockTypeImage(0, 4), false);

    private BufferedImage image;
    private boolean collideable;

    private BlockType(BufferedImage image, boolean collideable) {
        this.image = image;
        this.collideable = collideable;
    }

    public BufferedImage getImage() {
        return image;
    }
    public boolean isCollideable() {
        return collideable;
    }

}
