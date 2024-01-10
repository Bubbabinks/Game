package game.world;

import game.Player;
import game.Render;
import game.world.worldGenerator.WorldGenerator;
import main.FileManager;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class World implements Serializable {

    private static final int maxChunksInMemory = 50;

    private static World world;
    private static ArrayList<Chunk> chunksInMemory = new ArrayList<Chunk>();

    private Player client;
    private WorldGenerator worldGenerator;
    private String name;
    private int renderX = 0, renderY = 0, renderOffsetX = 0, renderOffsetY = 0;

    public transient WorldDetails worldDetails;

    public static void init() {

    }

    public static void createWorld(String name, WorldGenerator worldGenerator) {
        World world = new World(worldGenerator, name);
        world.worldDetails.setFilename(FileManager.getNextWorldFileName());
        loadWorld(world);
        save();
        FileManager.saveWorldDetails(world.worldDetails);
    }

    public static boolean loadWorld(String name) {
        World world = FileManager.findWorld(name);
        if (world == null) {
            return false;
        }else {
            loadWorld(world);
            return true;
        }
    }

    private static void loadWorld(World world) {
        Render.clearRenderedObject();
        Render.x = world.renderX;
        Render.y = world.renderY;
        Render.xoffset = world.renderOffsetX;
        Render.yoffset = world.renderOffsetY;
        Render.addRenderedObject(world.client);
        World.world = world;
        world.client.setWorld(world);
        world.client.init();
    }

    private World(WorldGenerator worldGenerator, String name) {
        this.worldGenerator = worldGenerator;
        worldDetails = new WorldDetails(name, null);
        client = new Player(world);

        this.name = name;
    }

    public static void save() {
        if (world != null) {
            world.renderX = Render.x;
            world.renderY = Render.y;
            world.renderOffsetX = Render.xoffset;
            world.renderOffsetY = Render.yoffset;
            FileManager.saveWorld(world);
            FileManager.saveWorldDetails(world.worldDetails);
        }
    }

    public static Player getClient() {
        return world.client;
    }

    public static BlockType getBlock(int x, int y) {
        Point point = getStartLocation(x, y);
        int cx = point.x;
        int cy = point.y;

        Chunk lc = loadChunk(x, y);
        return lc.getBlock(x-cx, y-cy);
    }

    public static void setBlock(int x, int y, BlockType block) {
        Point point = getStartLocation(x, y);
        int cx = point.x;
        int cy = point.y;

        Chunk lc = loadChunk(x, y);
        lc.setBlock(x-cx, y-cy, block);
    }

    public static Chunk loadChunk(int x, int y) {
        Point point = getStartLocation(x, y);
        x = point.x;
        y = point.y;

        Chunk lc = null;
        for (var chunk: chunksInMemory) {
            if (chunk.x == x && chunk.y == y) {
                lc = chunk;
                break;
            }
        }
        if (lc != null) {
            return lc;
        }

        Chunk chunk = FileManager.loadChunk(x, y, world);
        if (chunk != null) {
            if (chunksInMemory.size() >= maxChunksInMemory) {
                FileManager.saveChunk(world, chunksInMemory.remove(chunksInMemory.size()-1));
            }
            chunksInMemory.add(0, chunk);
            return chunk;
        }

        chunk = new Chunk(x, y);
        world.worldGenerator.generateChunk(chunk);
        if (chunksInMemory.size() >= maxChunksInMemory) {
            FileManager.saveChunk(world, chunksInMemory.remove(chunksInMemory.size()-1));
        }
        chunksInMemory.add(0, chunk);
        return chunk;
    }

    public static Point getStartLocation(int x, int y) {
        x = Math.floorDiv(x, Chunk.chunkSize)*Chunk.chunkSize;
        y = Math.floorDiv(y, Chunk.chunkSize)*Chunk.chunkSize;
        return new Point(x, y);
    }

    public static boolean isWorldLoaded() {
        return world != null;
    }

    public static ArrayList<Chunk> getChunksInMemory() {
        return chunksInMemory;
    }

}
