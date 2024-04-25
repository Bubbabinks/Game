package game.entity;

import game.KeyManager;
import game.Render;
import game.inventory.Inventory;
import game.inventory.ItemStack;
import game.inventory.PlayerInventory;
import game.update.Update;
import game.world.BlockType;
import game.world.World;
import main.Manager;

import java.awt.*;
import java.awt.event.*;

public class Player extends Entity {

    private final int speed = 4;
    private final int maxDistanceFromCenter = 200;

    private boolean flight = false;
    private boolean flightAllowed = false;

    private PlayerInventory inventory = new PlayerInventory();
    private transient Inventory openedInventory = null;
    public transient boolean drawUpperHotbar = true;

    private transient KeyListener keyListener;
    private transient MouseListener mouseListener;
    private transient MouseWheelListener mouseWheelListener;

    public Player() {
        super(EntityType.player);
        hasGravity = true;
        coord.y = 20;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void openInventory(Inventory inventory) {
        if (openedInventory != null) {
            closeInventory();
        }
        Update.pausePhysics = true;
        Render.renderedInventory = inventory;
        inventory.onOpen();
        openedInventory = inventory;
    }

    public void closeInventory() {
        Update.pausePhysics = false;
        openedInventory.onClose();
        openedInventory = null;
        Render.renderedInventory = null;
    }

    @Override
    public void onDeath() {
        Render.removeML(mouseListener);
        inventory.onPlayerKilled();
    }

    @Override
    protected void onDraw(Graphics g) {

    }

    public void init() {
        if (Manager.developmentMode) {
            flightAllowed = true;
        }
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (flightAllowed) {
                    if (e.getKeyCode() == KeyEvent.VK_X) {
                        flight = !flight;
                        hasGravity = !hasGravity;
                        if (flight) {
                            isJumping = false;
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    if (openedInventory == null) {
                        openInventory(inventory);
                    }else {
                        closeInventory();
                    }
                }
                if (e.getKeyCode() > 48 && e.getKeyCode() < 58) {
                    if (!Update.pausePhysics) {
                        inventory.setSelectedSlot(e.getKeyCode()-49);
                    }

                }
            }
        };
        mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!Update.pausePhysics) {
                    int x = e.getX();
                    int y = e.getY();
                    x = Math.floorDiv((x - Render.hsw) + Render.xoffset + Render.hbs, Render.bs) + Render.x;
                    y = Math.floorDiv(-(y - Render.hsh) + Render.yoffset + Render.hbs, Render.bs) + Render.y;
                    if (e.getButton() == 3) {
                        if (World.getBlock(x, y) == null) {
                            PlayerInventory pi = (PlayerInventory) inventory;
                            int selectedSlot = pi.getSelectedSlot();
                            if (pi.getItemStack(selectedSlot, 0) != null) {
                                World.setBlock(x, y, pi.getItemStack(selectedSlot, 0).getType().getBlockType());
                                if (!flight) {
                                    pi.removeOne(selectedSlot, 0);
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
                }else {
                    inventory.onMousePressed(e.getPoint());
                }
            }
        };
        mouseWheelListener = e -> {
            if (!Update.pausePhysics) {

                //Needs to be fixed!
                if (e.getWheelRotation() > 0) {
                    int desiredSelected = inventory.getSelectedSlot()+1;
                    if (desiredSelected > 8) {
                        inventory.setSelectedSlot(desiredSelected-9);
                    }else {
                        inventory.setSelectedSlot(desiredSelected);
                    }
                }else {
                    int desiredSelected = inventory.getSelectedSlot()-1;
                    if (desiredSelected < 0) {
                        inventory.setSelectedSlot(desiredSelected+9);
                    }else {
                        inventory.setSelectedSlot(desiredSelected);
                    }
                }
            }

        };
        KeyManager.addKeyListener(keyListener);
        Render.addML(mouseListener);
        Render.addMWL(mouseWheelListener);
    }

    @Override
    protected void physicsCall() {
        //where to draw hotbar
        if (coord.y > Render.y+2) {
            drawUpperHotbar = false;
        }else {
            drawUpperHotbar = true;
        }
        //Basic Movement
        if (KeyManager.input(KeyEvent.VK_D)) {
            moveRight(speed);
        }
        if (KeyManager.input(KeyEvent.VK_A)) {
            moveLeft(speed);
        }

        //Flight movement
        if (flight) {
            if (KeyManager.input(KeyEvent.VK_W)) {
                moveUp(speed);
            }
            if (KeyManager.input(KeyEvent.VK_S)) {
                moveDown(speed);
            }
        }else {
            if (KeyManager.input(KeyEvent.VK_W)) {
                tryJump();
            }
        }
        //Renderer movement this should be moved into the render at some point... and called by function from here
        //Right
        while (((coord.x-Render.x)*Render.bs+(coord.xoffset-Render.xoffset)) >= maxDistanceFromCenter) {
            Render.xoffset++;
        }
        while (Render.xoffset >= Render.bs) {
            Render.x++;
            Render.xoffset -= Render.bs;
        }
        //Left
        while (((coord.x-Render.x)*Render.bs+(coord.xoffset-Render.xoffset)) <= -maxDistanceFromCenter) {
            Render.xoffset--;
        }
        while (Render.xoffset < 0) {
            Render.x--;
            Render.xoffset += Render.bs;
        }
        //Up
        while (((coord.y-Render.y)*Render.bs+(coord.yoffset-Render.yoffset)) >= maxDistanceFromCenter) {
            Render.yoffset++;
        }
        while (Render.yoffset >= Render.bs) {
            Render.y++;
            Render.yoffset -= Render.bs;
        }
        //Down
        while (((coord.y-Render.y)*Render.bs+(coord.yoffset-Render.yoffset)) <= -maxDistanceFromCenter) {
            Render.yoffset--;
        }
        while (Render.yoffset < 0) {
            Render.y--;
            Render.yoffset += Render.bs;
        }
    }

}
