package game.entity;

import game.Render;
import game.world.World;

import java.awt.*;

public class Demon extends Entity {

    private final int speed = 2;

    private final int stepsDetectPlayer = 20;
    private final int minDistanceToPlayer = 250;
    private int detectPlayerStep = 0;
    private Coord target = null;
    private Entity desiredTarget = null;

    public Demon() {
        super(EntityType.demon);
        hasGravity = true;
    }

    @Override
    protected void physicsCall() {
        Player player = World.getClient();
        if (stepsDetectPlayer < detectPlayerStep) {
            if (!Collision.distanceGreaterThan(this, player, minDistanceToPlayer)) {
                RaycastResult result = raycast(player);
                if (result.status == RaycastResult.Status.noHit) {
                    target = new Coord(player.coord);
                    desiredTarget = player;
                }
            }
            detectPlayerStep = 0;
        }else {
            detectPlayerStep++;
        }
        if (target != null) {
            RaycastResult result = raycast(desiredTarget);
            if (result.status == RaycastResult.Status.noHit) {
                target = new Coord(player.coord);
            }
            if (target.getAbsoluteX() > coord.getAbsoluteX()) {
                int s = moveRight(speed);
                if (s == 0) {
                    tryJump();
                }
            }else if (target.getAbsoluteX() < coord.getAbsoluteX()) {
                int s = moveLeft(speed);
                if (s == 0) {
                    tryJump();
                }
            }else {
                target = null;
                desiredTarget = null;
            }

        }
    }

    @Override
    protected void onDeath() {

    }

    @Override
    protected void onDraw(Graphics g) {

    }
}
