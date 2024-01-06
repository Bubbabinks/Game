package game;

import java.awt.*;
import java.io.Serializable;

public abstract class GameObject implements Serializable {

    protected int width = 40;
    protected int height = 40;
    protected int x = 0;
    protected int y = 0;
    protected int xoffset = 0;
    protected int yoffset = 0;
    protected int halfWidth = width/2;
    protected int halfHeight = height/2;
    protected EntityType entityType;

}
