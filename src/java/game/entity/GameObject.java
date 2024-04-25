package game.entity;

import game.Render;
import game.world.World;

import java.io.Serializable;

public abstract class GameObject implements Serializable {

    public int width = Render.bs;
    public int height = Render.bs;
    public Coord coord = new Coord();
    public int halfWidth = width/2;
    public int halfHeight = height/2;
    public EntityType entityType;

    public void kill() {}

}
