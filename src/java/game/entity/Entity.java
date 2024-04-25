package game.entity;

import game.Render;
import game.world.World;

import java.awt.*;

public abstract class Entity extends GameObject{

    private final int gravitySpeed = 4;
    private final EntityType entityType;

    protected boolean isGrounded = false;
    protected boolean hasGravity = false;
    protected boolean isJumping = false;

    protected int maxJumpTime = 25;
    protected int jumpSpeed = gravitySpeed*2;

    private int jumpTime = 0;
    private boolean isInWorld = false;

    public Entity(EntityType type) {
        entityType = type;
    }

    public void addToWorld() {
        isInWorld = true;
        World.addEntity(this);
    }

    public void removeFromWorld() {
        isInWorld = false;
        World.removeEntity(this);
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
        coord.move(-s, 0);
        return s;
    }

    protected int moveRight(int distance) {
        int s = Collision.checkCollisionRight(this, distance);
        coord.move(s, 0);
        return s;
    }

    protected int moveUp(int distance) {
        int s = Collision.checkCollisionUp(this, distance);
        coord.move(0, s);
        return s;
    }

    protected int moveDown(int distance) {
        int s = Collision.checkCollisionDown(this, distance);
        coord.move(0, -s);
        if (s == 0) {
            isGrounded = true;
        }else {
            isGrounded = false;
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

    public RaycastResult raycast(Entity entity) {
        Coord c1 = new Coord(coord);
        Coord c2 = new Coord(entity.coord);
        c1.move(halfWidth, halfHeight);
        c2.move(entity.halfWidth, entity.halfHeight);
        return Collision.raycast(c1, c2);
    }

    public RaycastResult raycast(Coord c2) {
        Coord c1 = new Coord(coord);
        c1.move(halfWidth, halfHeight);
        return Collision.raycast(c1, c2);
    }

    @Override
    public void kill() {
        removeFromWorld();
        onDeath();
    }

    public void draw(Graphics g) {

        onDraw(g);
    }

    protected abstract void physicsCall();
    protected abstract void onDeath();
    protected abstract void onDraw(Graphics g);
}
