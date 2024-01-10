package game;

import game.world.BlockType;
import game.world.World;

public class Collision {

    public static int checkCollisionUp(GameObject gameObject, int distance) {
        BlockType block = World.getBlock(gameObject.x, gameObject.y+2);
        BlockType block2 = World.getBlock(gameObject.x+1, gameObject.y+2);
        if (gameObject.yoffset == 0) {
            BlockType block3 = World.getBlock(gameObject.x, gameObject.y+1);
            BlockType block4 = World.getBlock(gameObject.x+1, gameObject.y+1);
            if ((block3 == null || !block3.isCollideable()) && (block4 == null || !block4.isCollideable() || gameObject.xoffset == 0)) {
                return distance;
            }else {
                return 0;
            }
        }else {
            if ((block == null || !block.isCollideable()) && (block2 == null || !block2.isCollideable() || gameObject.xoffset == 0)) {
                return distance;
            }else {
                int d = Render.bs - gameObject.yoffset;
                if (d < distance) {
                    return d;
                }else {
                    return distance;
                }
            }

        }
    }

    public static int checkCollisionRight(GameObject gameObject, int distance) {
        BlockType block = World.getBlock(gameObject.x+2, gameObject.y);
        BlockType block2 = World.getBlock(gameObject.x+2, gameObject.y+1);
        if (gameObject.xoffset == 0) {
            BlockType block3 = World.getBlock(gameObject.x+1, gameObject.y);
            BlockType block4 = World.getBlock(gameObject.x+1, gameObject.y+1);
            if ((block3 == null ||!block3.isCollideable()) && (block4 == null || !block4.isCollideable() || gameObject.yoffset == 0)) {
                return distance;
            }else {
                return 0;
            }
        }else {
            if ((block == null || !block.isCollideable()) && (block2 == null || !block2.isCollideable() || gameObject.yoffset == 0)) {
                return distance;
            } else {
                int d = Render.bs - gameObject.xoffset;
                if (d < distance) {
                    return d;
                }else {
                    return distance;
                }
            }
        }
    }

    public static int checkCollisionDown(GameObject gameObject, int distance) {
        BlockType block = World.getBlock(gameObject.x, gameObject.y-1);
        BlockType block2 = World.getBlock(gameObject.x+1, gameObject.y-1);
        if ((block == null || !block.isCollideable()) && (block2 == null || !block2.isCollideable() || gameObject.xoffset == 0)) {
            return distance;
        } else {
            int d = gameObject.yoffset;
            if (d < distance) {
                return d;
            }else {
                return distance;
            }
        }

    }

    public static int checkCollisionLeft(GameObject gameObject, int distance) {
        BlockType block = World.getBlock(gameObject.x-1, gameObject.y);
        BlockType block2 = World.getBlock(gameObject.x-1, gameObject.y+1);
        if ((block == null || !block.isCollideable()) && (block2 == null || !block2.isCollideable() || gameObject.yoffset == 0)) {
            return distance;
        } else {
            int d = gameObject.xoffset;
            if (d < distance) {
                return d;
            }else {
                return distance;
            }
        }
    }

}
