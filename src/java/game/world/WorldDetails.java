package game.world;

import java.io.Serializable;

public class WorldDetails implements Serializable {

    private String name, filename;

    public WorldDetails(String name, String filename) {
        this.name = name;
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
