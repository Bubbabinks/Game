package game.entity;

import game.world.World;

public class Bunny extends Entity {

    private final int speed = 3;
    private final int stepsDetectPlayer = 20;
    private final int minDistanceToPlayer = 200;
    private final int maxDistanceToPlayer = 400;
    private int detectPlayerStep = 0;
    private boolean isRunningFromPlayer = false;


    public Bunny() {
        super(EntityType.bunny);
        hasGravity = true;
        jumpSpeed = 6;
    }

    @Override
    protected void physicsCall() {
        Player player = World.getClient();
        if (detectPlayerStep > stepsDetectPlayer) {
            if (isRunningFromPlayer) {
                if (Collision.distanceGreaterThan(player, this, maxDistanceToPlayer)) {
                    isRunningFromPlayer = false;
                }
            }else {
                if (!Collision.distanceGreaterThan(player, this, minDistanceToPlayer)) {
                    isRunningFromPlayer = true;
                }
            }
            detectPlayerStep = 0;
        }else {
            detectPlayerStep++;
        }

        if (isRunningFromPlayer) {
            if (!isGrounded) {
                if (player.x > x) {
                    moveLeft(speed);
                }else {
                    moveRight(speed);
                }
            }else {
                tryJump();
            }
        }

    }
}
