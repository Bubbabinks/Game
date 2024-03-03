package game.world;

import game.inventory.ItemType;
import main.FileManager;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public enum BlockType implements Serializable {

    stone(FileManager.getBlockTypeImage(0,0), true),
    dirt(FileManager.getBlockTypeImage(0, 1), true),
    grass(FileManager.getBlockTypeImage(0, 2), true),
    log(FileManager.getBlockTypeImage(0, 3), false),
    leaves(FileManager.getBlockTypeImage(0, 4), false),
    deep_stone(FileManager.getBlockTypeImage(0, 5), true),
    water(FileManager.getBlockTypeImage(0,6), false),
    sand(FileManager.getBlockTypeImage(0, 7), true);

    private BufferedImage image;
    private boolean collideable;
    private ItemType drop;

    public static void init() {
        //setting drops
        stone.drop = ItemType.stone;
        dirt.drop = ItemType.dirt;
        grass.drop = ItemType.dirt;
        log.drop = ItemType.log;
        deep_stone.drop = ItemType.deep_stone;
        sand.drop = ItemType.sand;
    }

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

    public ItemType getDrop() {
        return drop;
    }

}
