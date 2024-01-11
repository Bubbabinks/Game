package game.entity;

import game.KeyManager;
import game.Render;
import game.inventory.Inventory;
import game.inventory.ItemStack;
import game.inventory.PlayerInventory;
import game.update.OnUpdate;
import game.update.Update;
import game.world.BlockType;
import game.world.World;
import main.Manager;

import java.awt.*;
import java.awt.event.*;

public class Player extends GameObject {

    private int speed = 4;
    private int maxJumpTime = 200/(speed*2);
    private int maxDistanceFromCenter = 200;

    public boolean isGrounded = false;
    public boolean isJumping = false;
    private int jumpTime = 0;
    private boolean flight = false;
    private boolean flightAllowed = false;

    private Inventory inventory = new PlayerInventory();
    private transient OnUpdate onUpdate = this::movementThread;
    private transient MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                x = Math.floorDiv((x - Render.hsw)+Render.xoffset+Render.hbs,Render.bs)+Render.x;
                y = Math.floorDiv(-(y - Render.hsh)+Render.yoffset+Render.hbs,Render.bs)+Render.y;
                if (e.getButton() == 3) {
                    if (World.getBlock(x, y) == null) {
                        PlayerInventory pi = (PlayerInventory) inventory;
                        Point selectedSlot = pi.getSelectedSlot();
                        if (pi.getItemStack(selectedSlot.x, selectedSlot.y) != null) {
                            World.setBlock(x, y, pi.getItemStack(selectedSlot.x, selectedSlot.y).getType().getBlockType());
                            if (!flight) {
                                pi.removeOne(selectedSlot.x, selectedSlot.y);
                            }
                        }
                    }
                }
                if (e.getButton() == 1) {
                    BlockType block = World.getBlock(x, y);
                    if (block != null) {
                        if (block.getDrop() != null && !flight) {
                            inventory.addItemStack(new ItemStack(block.getDrop(), 1));
                        }
                        World.setBlock(x, y, null);
                    }

                }
                Render.regatherWorld();
            }
    };

    public Player(World world) {
        super(world);
        y = 20;
        entityType = EntityType.player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void kill() {
        Update.removePhysicsUpdate(onUpdate);
        Render.removeML(mouseListener);
        ((PlayerInventory)inventory).onPlayerKilled();
    }

    public void init() {
        if (Manager.developmentMode) {
            flightAllowed = true;
        }
        Update.addPhysicsUpdate(onUpdate);
        KeyManager.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (flightAllowed) {
                    if (e.getKeyCode() == KeyEvent.VK_X) {
                        flight = !flight;
                    }
                }
            }
        });
        Render.addML(mouseListener);
    }

    private void movementThread() {
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


        if (flight) {
            if (KeyManager.input(KeyEvent.VK_W)) {
                yoffset += Collision.checkCollisionUp(this, speed);
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

            }
            if (KeyManager.input(KeyEvent.VK_S)) {
                yoffset -= Collision.checkCollisionDown(this, speed);
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
            }
        }else {
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
                if (jumpTime < maxJumpTime) {
                    yoffset += Collision.checkCollisionUp(this, speed*2);
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
        }
    }

}
