package game.entity;

import game.Render;
import game.world.BlockType;
import game.world.World;

import java.awt.*;

public class Collision {

    public static int checkCollisionUp(GameObject gameObject, int distance) {
        if (World.isWorldLoaded()) {
            Coord c = gameObject.coord;
            BlockType block = World.getBlock(c.x, c.y+2);
            BlockType block2 = World.getBlock(c.x+1, c.y+2);
            if (c.yoffset == 0) {
                BlockType block3 = World.getBlock(c.x, c.y+1);
                BlockType block4 = World.getBlock(c.x+1, c.y+1);
                if ((block3 == null || !block3.isCollideable()) && (block4 == null || !block4.isCollideable() || c.xoffset == 0)) {
                    return distance;
                }else {
                    return 0;
                }
            }else {
                if ((block == null || !block.isCollideable()) && (block2 == null || !block2.isCollideable() || c.xoffset == 0)) {
                    return distance;
                }else {
                    int d = Render.bs - c.yoffset;
                    if (d < distance) {
                        return d;
                    }else {
                        return distance;
                    }
                }

            }
        }
        return 0;
    }

    public static int checkCollisionRight(GameObject gameObject, int distance) {
        if (World.isWorldLoaded()) {
            Coord c = gameObject.coord;
            BlockType block = World.getBlock(c.x+2, c.y);
            BlockType block2 = World.getBlock(c.x+2, c.y+1);
            if (c.xoffset == 0) {
                BlockType block3 = World.getBlock(c.x+1, c.y);
                BlockType block4 = World.getBlock(c.x+1, c.y+1);
                if ((block3 == null ||!block3.isCollideable()) && (block4 == null || !block4.isCollideable() || c.yoffset == 0)) {
                    return distance;
                }else {
                    return 0;
                }
            }else {
                if ((block == null || !block.isCollideable()) && (block2 == null || !block2.isCollideable() || c.yoffset == 0)) {
                    return distance;
                } else {
                    int d = Render.bs - c.xoffset;
                    if (d < distance) {
                        return d;
                    }else {
                        return distance;
                    }
                }
            }
        }
        return 0;
    }

    public static int checkCollisionDown(GameObject gameObject, int distance) {
        if (World.isWorldLoaded()) {
            Coord c = gameObject.coord;
            BlockType block = World.getBlock(c.x, c.y-1);
            BlockType block2 = World.getBlock(c.x+1, c.y-1);
            if ((block == null || !block.isCollideable()) && (block2 == null || !block2.isCollideable() || c.xoffset == 0)) {
                return distance;
            } else {
                int d = c.yoffset;
                if (d < distance) {
                    return d;
                }else {
                    return distance;
                }
            }
        }
        return 0;

    }

    public static int checkCollisionLeft(GameObject gameObject, int distance) {
        if (World.isWorldLoaded()) {
            Coord c = gameObject.coord;
            BlockType block = World.getBlock(c.x-1, c.y);
            BlockType block2 = World.getBlock(c.x-1, c.y+1);
            if ((block == null || !block.isCollideable()) && (block2 == null || !block2.isCollideable() || c.yoffset == 0)) {
                return distance;
            } else {
                int d = c.xoffset;
                if (d < distance) {
                    return d;
                }else {
                    return distance;
                }
            }
        }
        return 0;
    }

    public static boolean distanceGreaterThan(GameObject o1, GameObject o2, double distance) {
        distance *= distance;
        double x = (o2.coord.x*Render.bs+o2.coord.xoffset)-(o1.coord.x*Render.bs+o1.coord.xoffset);
        double y = (o2.coord.y*Render.bs+o2.coord.yoffset)-(o1.coord.y*Render.bs+o1.coord.yoffset);
        double d = Math.pow(x, 2) + Math.pow(y, 2);
        return d > distance;
    }

    public static RaycastResult raycast(Coord p1, Coord p2) {
        RaycastResult raycastResult = new RaycastResult();
        raycastResult.origin = p1;
        raycastResult.target = p2;
        if (World.isWorldLoaded()) {
            long minx = p1.getAbsoluteX();
            long maxx = p2.getAbsoluteX();
            long miny = p1.getAbsoluteY();
            long maxy = p2.getAbsoluteY();
            long direction = (maxx - minx);
            if (direction != 0) {
                direction = direction / Math.abs(direction);
            }else {
                raycastResult.status = RaycastResult.Status.divisionByZero;
                return raycastResult;
            }
            Coord pc = new Coord(p1);
            long x = minx;
            while (x != maxx) {
                long y = (long)inverseLerp(miny, maxy, lerp(minx, maxx, x));
                Coord cc = new Coord(x, y);
                BlockType block = World.getBlock(cc.x, cc.y);
                if (block != null) {
                    raycastResult.status = RaycastResult.Status.hit;
                    raycastResult.hitCoord = pc;
                    return raycastResult;
                }
                pc.setPos(cc);
                x += direction;
            }
            raycastResult.status = RaycastResult.Status.noHit;
        }else {
            raycastResult.status = RaycastResult.Status.worldNotInit;
        }
        return raycastResult;
    }

    private static double lerp(double a, double b, double t) {
        t = t - a;
        b = b - a;
        return t / b;
    }

    private static double inverseLerp(double a, double b, double p) {
        b = b - a;
        return (p * b) + a;
    }

}
