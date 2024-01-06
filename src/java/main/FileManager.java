package main;

import game.Render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FileManager {

    private final static String mainFolder = System.getProperty("user.home")+"/Desktop/Game/";

    private static Image blockSheet = loadInternalImage("block/block_sheet");
    private static Image entitySheet = loadInternalImage("entity/entity_sheet");

    public static void init() {
        File file = new File(mainFolder);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(mainFolder+"worlds");

    }

    private static Image loadInternalImage(String path) {
        try {
            return ImageIO.read(FileManager.class.getClassLoader().getResourceAsStream("textures/"+path+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

}
