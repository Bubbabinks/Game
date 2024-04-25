package game.entity;

import game.Render;
import game.world.World;

public abstract class Entity extends GameObject{

    private final int gravitySpeed = 4;
    private final EntityType entityType;

    protected boolean isGrounded = false;
    protected boolean hasGravity = false;
    protected boolean isJumping = false;

    protected int maxJumpTime = 25;
    protected int jumpSpeed = gravitySpeed*2;

    private int jumpTime = 0;

    public Entity(EntityType type) {
        entityType = type;
        World.addEntity(this);
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void onPhysicsUpdate() {
        if (hasGravity) {
            moveDown(gravitySpeed);
        }

        if (isJumping) {
            if (jumpTime < maxJumpTime) {
                int s = moveUp(jumpSpeed);
                if (s == 0) {
                    isJumping = false;
                }
                jumpTime++;
            }else {
                isJumping = false;
            }
        }

        if (World.isWorldLoaded()) {
            physicsCall();
        }
    }

    protected int moveLeft(int distance) {
        int s = Collision.checkCollisionLeft(this, distance);
        xoffset -= s;
        while (xoffset < 0) {
            x--;
            xoffset += Render.bs;
        }
        return s;
    }

    protected int moveRight(int distance) {
        int s = Collision.checkCollisionRight(this, distance);
        xoffset += s;
        while (xoffset >= Render.bs) {
            x++;
            xoffset -= Render.bs;
        }
        return s;
    }

    protected int moveUp(int distance) {
        int s = Collision.checkCollisionUp(this, distance);
        yoffset += s;
        while (yoffset >= Render.bs) {
            y++;
            yoffset -= Render.bs;
        }
        return s;
    }

    protected int moveDown(int distance) {
        int s = Collision.checkCollisionDown(this, distance);
        yoffset -= s;
        if (s == 0) {
            isGrounded = true;
        }else {
            isGrounded = false;
        }
        while (yoffset < 0) {
            y--;
            yoffset += Render.bs;
        }
        return s;
    }

    protected boolean tryJump() {
        if (isJumping || !isGrounded) {
            return false;
        }
        jumpTime = 0;
        isJumping = true;
        return true;
    }

    protected abstract void physicsCall();
}
