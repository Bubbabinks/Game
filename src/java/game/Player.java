package game;

import game.world.BlockType;
import game.world.World;
import main.Manager;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Player extends GameObject {

    private int speed = 1;
    private int maxDistanceFromCenter = 200;

    public boolean isGrounded = false;
    public boolean isJumping = false;
    private int jumpTime = 0;

    public Player(World world) {
        super(world);
        entityType = EntityType.player;
    }

    public void init() {
        Thread playerMovement = new Thread(this::movementThread);
        playerMovement.start();
        Render.addML(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                x = Math.floorDiv((x - Render.hsw)+Render.xoffset+Render.hbs,Render.bs)+Render.x;
                y = Math.floorDiv(-(y - Render.hsh)+Render.yoffset+Render.hbs,Render.bs)+Render.y;
                if (e.getButton() == 3) {
                    World.setBlock(x, y, BlockType.stone);
                }
                if (e.getButton() == 1) {
                    World.setBlock(x, y, null);
                }
                Render.regatherWorld();
            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
        });
    }

    private void movementThread() {
        while (Manager.applicationRunning) {
            if (KeyManager.input(KeyEvent.VK_W)) {
                if (!isJumping && isGrounded) {
                    isJumping = true;
                    jumpTime = 0;
                }
            }
            if (KeyManager.input(KeyEvent.VK_D)) {
                xoffset += Collision.checkCollisionRight(this, speed);
                while (xoffset >= Render.bs) {
                    x++;
                    xoffset -= Render.bs;
                }
                while (((x-Render.x)*Render.bs+(xoffset-Render.xoffset)) >= maxDistanceFromCenter) {
                    Render.xoffset++;
                }
                while (Render.xoffset >= Render.bs) {
                    Render.x++;
                    Render.xoffset -= Render.bs;
                }

            }
            if (KeyManager.input(KeyEvent.VK_A)) {
                xoffset -= Collision.checkCollisionLeft(this, speed);
                while (xoffset < 0) {
                    x--;
                    xoffset += Render.bs;
                }
                while (((x-Render.x)*Render.bs+(xoffset-Render.xoffset)) <= -maxDistanceFromCenter) {
                    Render.xoffset--;
                }
                while (Render.xoffset < 0) {
                    Render.x--;
                    Render.xoffset += Render.bs;
                }
            }

            //Gravity
            int s = Collision.checkCollisionDown(this, speed);
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
            while (((y-Render.y)*Render.bs+(yoffset-Render.yoffset)) <= -maxDistanceFromCenter) {
                Render.yoffset--;
            }
            while (Render.yoffset < 0) {
                Render.y--;
                Render.yoffset += Render.bs;
            }

            //jump
            if (isJumping) {
                if (jumpTime < 100) {
                    yoffset += Collision.checkCollisionUp(this, speed+1);
                    while (yoffset >= Render.bs) {
                        y++;
                        yoffset -= Render.bs;
                    }
                    while (((y-Render.y)*Render.bs+(yoffset-Render.yoffset)) >= maxDistanceFromCenter) {
                        Render.yoffset++;
                    }
                    while (Render.yoffset >= Render.bs) {
                        Render.y++;
                        Render.yoffset -= Render.bs;
                    }
                    jumpTime++;
                }else {
                    isJumping = false;
                }
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
