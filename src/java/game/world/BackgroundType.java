package game.world;

import main.FileManager;

import java.awt.*;
import java.io.File;
import java.io.Serializable;

public enum BackgroundType implements Serializable {

    sky(FileManager.getBackgroundTypeImage(0, 0)),
    underground(FileManager.getBackgroundTypeImage(0, 1)),
    deep_underground(FileManager.getBackgroundTypeImage(0, 2));

    private Image image;

    private BackgroundType(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

}
