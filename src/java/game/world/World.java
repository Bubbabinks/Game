package game.world;

import game.Player;
import game.Render;
import game.world.worldGenerator.CheckerBoard;
import game.world.worldGenerator.EarthLikeGenerator;
import game.world.worldGenerator.WorldGenerator;

import java.io.Serializable;

public class World implements Serializable {

    private static World world;
    private static Chunk[] loadedChunks = new Chunk[9];

    private Player player;
    private WorldGenerator worldGenerator;
    private String name;

    public static void init() {
        world = new World(new EarthLikeGenerator(), "Test");

        Render.addRenderedObject(world.player);
    }

    private World(WorldGenerator worldGenerator, String name) {
        this.worldGenerator = worldGenerator;

        for (int i = 0; i < loadedChunks.length; i++) {
            loadedChunks[i] = new Chunk(((i%3)-1)*Chunk.chunkSize, ((i/3)-1)*Chunk.chunkSize);
            worldGenerator.generateChunk(loadedChunks[i]);
        }
        player = new Player();

        this.name = name;
    }

    public static Player getPlayer() {
        return world.player;
    }

    public static BlockType getBlock(int x, int y) {
        int cx = Math.floorDiv(x, Chunk.chunkSize)*Chunk.chunkSize;
        int cy = Math.floorDiv(y, Chunk.chunkSize)*Chunk.chunkSize;

        Chunk lc = null;
        for (int i = 0; i < loadedChunks.length; i++) {
            if (loadedChunks[i].x == cx && loadedChunks[i].y == cy) {
                lc = loadedChunks[i];
                break;
            }
        }
        if (lc != null) {
            return lc.getBlock(x-cx, y-cy);
        }
        return null;
    }

    public static void setBlock(int x, int y, BlockType block) {
        int cx = Math.floorDiv(x, Chunk.chunkSize)*Chunk.chunkSize;
        int cy = Math.floorDiv(y, Chunk.chunkSize)*Chunk.chunkSize;

        Chunk lc = null;
        for (int i = 0; i < loadedChunks.length; i++) {
            if (loadedChunks[i].x == cx && loadedChunks[i].y == cy) {
                lc = loadedChunks[i];
                break;
            }
        }
        if (lc != null) {
            lc.setBlock(x-cx, y-cy, block);
        }
    }

    public static Chunk loadChunk(int x, int y) {
        x = Math.floorDiv(x, Chunk.chunkSize)*Chunk.chunkSize;
        y = Math.floorDiv(y, Chunk.chunkSize)*Chunk.chunkSize;

        Chunk lc = null;
        for (int i = 0; i < loadedChunks.length; i++) {
            if (loadedChunks[i].x == x && loadedChunks[i].y == y) {
                lc = loadedChunks[i];
                break;
            }
        }
        if (lc != null) {
            return lc;
        }



        Chunk chunk = new Chunk(x, y);
        world.worldGenerator.generateChunk(chunk);

        return chunk;
    }

}
