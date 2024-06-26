package game.world;

import game.entity.Entity;
import game.entity.GameObject;
import game.entity.Player;
import game.Render;
import game.update.Update;
import game.world.worldGenerator.WorldGenerator;
import main.FileManager;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class World implements Serializable {

    private static final int maxChunksInMemory = 25;

    private static World world;
    private static ArrayList<Chunk> chunksInMemory = new ArrayList<Chunk>();

    private Player client;
    private ArrayList<Entity> entities;
    private WorldGenerator worldGenerator;
    private String name;
    private int renderX = 0, renderY = 0, renderOffsetX = 0, renderOffsetY = 0;

    public transient WorldDetails worldDetails;

    private static boolean isLoaded = false;

    public static void init() {
        Update.addPhysicsUpdate(()->{
            if (isWorldLoaded()) {
                for (int i=0; i<world.entities.size(); i++) {
                    world.entities.get(i).onPhysicsUpdate();
                }
            }
        });
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
        Render.x = world.renderX;
        Render.y = world.renderY;
        Render.xoffset = world.renderOffsetX;
        Render.yoffset = world.renderOffsetY;
        World.world = world;
        world.client.init();
        isLoaded = true;
    }

    private World(WorldGenerator worldGenerator, String name) {
        world = this;
        this.worldGenerator = worldGenerator;
        worldDetails = new WorldDetails(name, null);
        entities = new ArrayList<>();
        client = new Player();
        client.addToWorld();

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

    public static BackgroundType getBackground(int x, int y) {
        Point point = getStartLocation(x, y);
        int cx = point.x;
        int cy = point.y;

        Chunk lc = loadChunk(x, y);
        return lc.getBackground(x-cx, y-cy);
    }

    public static void setBackgroundType(int x, int y, BackgroundType background) {
        Point point = getStartLocation(x, y);
        int cx = point.x;
        int cy = point.y;

        Chunk lc = loadChunk(x, y);
        lc.setBackground(x-cx, y-cy, background);
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
        return isLoaded;
    }

    public static ArrayList<Chunk> getChunksInMemory() {
        return chunksInMemory;
    }

    public static void addEntity(Entity object) {
        world.entities.add(object);
    }

    public static void removeEntity(Entity object) {
        world.entities.remove(object);
    }

    public static List<Entity> getEntities() {
        return world.entities;
    }

}
