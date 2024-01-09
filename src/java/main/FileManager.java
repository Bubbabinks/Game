package main;

import game.Render;
import game.world.Chunk;
import game.world.World;
import game.world.WorldDetails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileManager {

    private final static String mainFolder = System.getProperty("user.home")+"/Desktop/Game/";
    private final static ArrayList<WorldDetails> worldDetails = new ArrayList<WorldDetails>();

    private static Image blockSheet = loadInternalImage("block/block_sheet");
    private static Image entitySheet = loadInternalImage("entity/entity_sheet");

    public static void init() {
        File file = new File(mainFolder);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(mainFolder+"Worlds");
        if (!file.exists()) {
            file.mkdir();
        }
        initWorldDetails();
        initSkyBoxes();
    }

    public static Image loadInternalImage(String path) {
        try {
            return ImageIO.read(FileManager.class.getClassLoader().getResourceAsStream("textures/"+path+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Image loadWorldImage(WorldDetails worldDetails) {
        try {
            return ImageIO.read(new File(mainFolder+"Worlds/"+worldDetails.getFilename()+"/icon.png"));
        } catch (IOException e) {}
        return null;
    }

    public static ImageUtil getImage(String path) {
        return new ImageUtil(loadInternalImage(path));
    }

    public static BufferedImage getBlockTypeImage(int x, int y) {
        return getTypeSubImage(x, y, blockSheet);
    }

    public static BufferedImage getEntityTypeImage(int x, int y) {
        return getTypeSubImage(x, y, entitySheet);
    }

    private static BufferedImage getTypeSubImage(int x, int y, Image original) {
        BufferedImage image = (BufferedImage) original;
        return image.getSubimage(x*Render.bs, y*Render.bs, Render.bs, Render.bs);
    }

    private static void writeObject(Object object, String path) {
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(path));
            objectOutput.writeObject(object);
            objectOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Object readObject(String path) {
        try {
            ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(path));
            Object o = objectInput.readObject();
            objectInput.close();
            return o;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void initWorldDetails() {
        File file = new File(mainFolder+"Worlds/");
        if (file.exists()) {
            for (var f: file.listFiles()) {
                worldDetails.add((WorldDetails)readObject(f.getPath()+"/World Details.detail"));
            }
        }
    }

    public static void saveWorldDetails(WorldDetails worldDetails) {
        File file = new File(mainFolder+"Worlds/"+worldDetails.getFilename()+"/World Details.detail");
        if (file.exists()) {
            file.delete();
        }
        writeObject(worldDetails, file.getPath());
    }

    public static String getNextWorldFileName() {
        File file = new File(mainFolder+"Worlds");
        boolean successful = false;
        int i = 0;
        while (!successful) {
            boolean s = false;
            for (var f: file.listFiles()) {
                if (f.getName().equals(i+"")) {
                    s = true;
                    break;
                }
            }
            if (s) {
                i++;
            }else {
                successful = true;
            }
        }
        return i+"";
    }

    public static void saveChunk(World world, Chunk chunk) {
        File file = new File(mainFolder+"Worlds/"+world.worldDetails.getFilename()+"/Regions/"+(chunk.x/Chunk.chunkSize)+" "+(chunk.y/Chunk.chunkSize)+".chunk");
        if (file.exists()) {
            file.delete();
        }
        writeObject(chunk, file.getPath());
    }

    public static void saveWorld(World world) {
        File file = new File(mainFolder+"Worlds/"+world.worldDetails.getFilename());
        if (!file.exists()) {
            file.mkdir();
        }
        writeObject(world, file.getPath()+"/s.world");
        file = new File(file.getPath()+"/Regions");
        if (!file.exists()) {
            file.mkdir();
        }
        for (var chunk: World.getChunksInMemory()) {
            saveChunk(world, chunk);
        }
        Image image = loadWorldImage(world.worldDetails);
        if (image == null) {
            try {
                Files.copy(FileManager.class.getClassLoader().getResourceAsStream("textures/default/worldImage.png"),
                        new File(mainFolder+"Worlds/"+world.worldDetails.getFilename()+"/icon.png").toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static World findWorld(String name) {
        for (var details: worldDetails) {
            if (details.getName().equals(name)) {
                World world = (World)readObject(mainFolder+"Worlds/"+details.getFilename()+"/s.world");
                world.worldDetails = details;
                return world;
            }
        }
        return null;
    }

    public static Chunk loadChunk(int x, int y, World world) {
        x = x/Chunk.chunkSize;
        y = y/Chunk.chunkSize;
        File file = new File(mainFolder+"Worlds/"+world.worldDetails.getFilename()+"/Regions/"+x+" "+y+".chunk");
        if (file.exists()) {
            return (Chunk)readObject(file.getPath());
        }
        return null;
    }

    public static void initSkyBoxes() {
        for (var sky: SkyBox.values()) {
            sky.setImage(loadInternalImage("skybox/"+sky.name()));
        }
    }

    public static WorldDetails findWorldDetails(String name) {
        for (var d: worldDetails) {
            if (name.equals(d.getName())) {
                return d;
            }
        }
        return null;
    }

    public static ArrayList<WorldDetails> getWorldDetails() {
        return worldDetails;
    }
}
