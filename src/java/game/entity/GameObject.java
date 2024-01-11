package game.entity;

import game.world.World;

import java.io.Serializable;

public abstract class GameObject implements Serializable {

    public GameObject(World world) {
        this.world = world;
    }

    public int width = 40;
    public int height = 40;
    public int x = 0;
    public int y = 0;
    public int xoffset = 0;
    public int yoffset = 0;
    public int halfWidth = width/2;
    public int halfHeight = height/2;
    public EntityType entityType;
    protected transient World world;

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void kill() {}

}
