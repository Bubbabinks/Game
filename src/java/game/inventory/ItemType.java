package game.inventory;

import game.world.BlockType;
import main.FileManager;

import java.awt.*;
import java.io.Serializable;

public enum ItemType implements Serializable {

    stone(FileManager.getItemTypeImage(0, 0), "Stone"),
    dirt(FileManager.getItemTypeImage(0, 1), "Dirt"),
    grass(FileManager.getItemTypeImage(0, 2), "Grass"),
    log(FileManager.getItemTypeImage(0, 3), "Log"),
    leaves(FileManager.getItemTypeImage(0, 4), "Leaves"),
    deep_stone(FileManager.getItemTypeImage(0, 5), "Deep Stone"),
    sand(FileManager.getItemTypeImage(0, 6), "Sand");

    private final Image image;
    private final String displayName;
    private BlockType blockType;

    public static void init() {
        //setting blocktypes
        stone.blockType = BlockType.stone;
        dirt.blockType = BlockType.dirt;
        grass.blockType = BlockType.grass;
        log.blockType = BlockType.placeableLog;
        leaves.blockType = BlockType.leaves;
        deep_stone.blockType = BlockType.deep_stone;
        sand.blockType = BlockType.sand;
    }

    ItemType(Image image, String displayName) {
        this.image = image;
        this.displayName = displayName;
    }

    public Image getImage() {
        return image;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BlockType getBlockType() {
        return blockType;
    }

}
