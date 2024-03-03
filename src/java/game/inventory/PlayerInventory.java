package game.inventory;

import game.Render;
import game.update.OnUpdate;
import game.update.Update;
import game.world.BlockType;
import main.FileManager;
import main.ImageUtil;
import ui.WindowManager;

import java.awt.*;
import java.awt.event.MouseWheelListener;

public class PlayerInventory extends Inventory {

    private static final ImageUtil SELECTED_ITEM_BACKGROUND = new ImageUtil(FileManager.loadInternalImage("inventory/item_selected_background"));
    private int selectedSlot = 0;
    private ItemStack itemOnMouse;
    private Point previousMouseLocation;

    public PlayerInventory() {
        super(9, 4);
        for (int x=0; x<9; x++) {
            for (int y=1; y<4; y++) {
                slots[y*9+x].setCenterY(y-2);
            }
        }
        for (int i=0; i<9; i++) {
            slots[i].setCenterY(0);
        }
        slots[0].setSelected(true);
    }

    public void onMousePressed(Point location) {
        for (int i = 0; i < 36; i++) {
            if (slots[i].isClickInBounds(location)) {
                if (itemOnMouse == null) {
                    itemOnMouse = slots[i].getItemStack();
                    slots[i].setItemStack(null);
                }else {
                    ItemStack itemStack = slots[i].getItemStack();
                    if (itemStack == null) {
                        slots[i].setItemStack(itemOnMouse);
                        itemOnMouse = null;
                    }else {
                        if (itemStack.getType() == itemOnMouse.getType()) {
                            int amountToMove = itemOnMouse.getAmount();
                            amountToMove = itemStack.addAmount(amountToMove);
                            if (amountToMove == 0) {
                                itemOnMouse = null;
                            }else {
                                itemOnMouse.setAmount(amountToMove);
                            }
                        }else {
                            slots[i].setItemStack(itemOnMouse);
                            itemOnMouse = itemStack;
                        }
                    }
                }
            }
        }
    }

    public void onPlayerKilled() {

    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int slot) {
        if (slot > -1 && slot < 9) {
            slots[selectedSlot].setSelected(false);
            selectedSlot = slot;
            slots[slot].setSelected(true);
        }
    }

    @Override
    public void draw(Graphics g) {
        for (int x=0; x<9; x++) {
            for (int y=1; y<4; y++) {
                slots[y*9+x].draw(g);
            }
        }
        if (itemOnMouse != null) {
            Point mouse = Render.getMousePos();
            if (mouse != null) {
                previousMouseLocation = mouse;
            }
            ImageUtil imageUtil = new ImageUtil(itemOnMouse.getType().getImage());
            int x = previousMouseLocation.x-imageUtil.width/2;
            int y = previousMouseLocation.y-imageUtil.height/2;
            g.drawImage(imageUtil.image, x, y, imageUtil.width, imageUtil.height, null);
            g.drawString(itemOnMouse.getAmount()+"",x+4, y+14);
        }
    }

    public void drawUpperHotbar(Graphics g) {
        for (int i=0; i<9; i++) {
            slots[i].alignNorth();
            slots[i].draw(g);
        }
    }

    public void drawLowerHotBar(Graphics g) {
        for (int i=0; i<9; i++) {
            slots[i].alignSouth();
            slots[i].draw(g);
        }
    }
}
