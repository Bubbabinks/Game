package game.world;

import main.FileManager;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public enum BlockType implements Serializable {

    stone(FileManager.getBlockTypeImage(0,0)),
    dirt(FileManager.getBlockTypeImage(0, 1)),
    grass(FileManager.getBlockTypeImage(0, 2)),
    log(FileManager.getBlockTypeImage(0, 3)),
    leaves(FileManager.getBlockTypeImage(0, 4));

    private BufferedImage image;

    private BlockType(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

}
