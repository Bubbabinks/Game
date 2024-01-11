package game.world;

import java.io.Serializable;

public class Chunk implements Serializable {

    public static final int chunkSize = 100;

    public int x, y;
    private BlockType[][] blocks;
    private BackgroundType[][] backgrounds;

    protected Chunk(int x, int y) {
        this.x = x; this.y = y;
        blocks = new BlockType[chunkSize][chunkSize];
        backgrounds = new BackgroundType[chunkSize][chunkSize];
    }

    public void setBlock(int x, int y, BlockType block) {
        blocks[x][y] = block;
    }

    public BlockType getBlock(int x, int y) {
        return blocks[x][y];
    }

    public void setBackground(int x, int y, BackgroundType background) {
        backgrounds[x][y] = background;
    }

    public BackgroundType getBackground(int x, int y) {
        return backgrounds[x][y];
    }

}
