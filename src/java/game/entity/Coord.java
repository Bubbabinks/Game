package game.entity;

import game.Render;

import java.io.Serializable;

public class Coord implements Serializable {

    public int x;
    public int y;
    public int xoffset;
    public int yoffset;

    public Coord() {}

    public Coord(int x, int y) {
        setPos(x, y);
    }

    public Coord(int x, int y, int xoffset, int yoffset) {
        setPos(x, y, xoffset, yoffset);
    }

    public Coord(Coord coord) {
        setPos(coord);
    }

    public Coord(long x, long y) {
        setPos(x, y);
    }

    public long getAbsoluteX() {
        return ((long) x)* ((long)Render.bs)+ ((long)xoffset);
    }

    public long getAbsoluteY() {
        return ((long) y)* ((long)Render.bs)+ ((long)yoffset);
    }

    public void move(int x, int y) {
        xoffset += x;
        yoffset += y;
        //up
        while (yoffset >= Render.bs) {
            this.y++;
            yoffset -= Render.bs;
        }
        //down
        while (yoffset < 0) {
            this.y--;
            yoffset += Render.bs;
        }
        //right
        while (xoffset >= Render.bs) {
            this.x++;
            xoffset -= Render.bs;
        }
        //left
        while (xoffset < 0) {
            this.x--;
            xoffset += Render.bs;
        }
    }

    public void setPos(long x, long y) {
        this.x = (int)(Math.floorDiv(x, Render.bs));
        this.y = (int)(Math.floorDiv(y, Render.bs));
        this.xoffset = (Math.floorMod(x, Render.bs));
        this.yoffset = (Math.floorMod(y, Render.bs));
    }

    public void setPos(int x, int y, int xoffset, int yoffset) {
        this.x = x;
        this.y = y;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPos(Coord coord) {
        this.x = coord.x;
        this.y = coord.y;
        this.xoffset = coord.xoffset;
        this.yoffset = coord.yoffset;
    }

}
