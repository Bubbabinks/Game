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

    private PlayerInventory inventory = new PlayerInventory();
    private transient Inventory openedInventory = null;
    public transient boolean drawUpperHotbar = true;

    private transient KeyListener keyListener;
    private transient MouseListener mouseListener;
    private transient MouseWheelListener mouseWheelListener;

    public Player(World world) {
        super(world);
        y = 20;
        entityType = EntityType.player;
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
    public void kill() {
        Update.removePhysicsUpdate(this::movementThread);
        Render.removeML(mouseListener);
        inventory.onPlayerKilled();
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
        Update.addPhysicsUpdate(this::movementThread);
        KeyManager.addKeyListener(keyListener);
        Render.addML(mouseListener);
        Render.addMWL(mouseWheelListener);
    }

    private void movementThread() {
        if (y > Render.y+2) {
            drawUpperHotbar = false;
        }else {
            drawUpperHotbar = true;
        }
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
