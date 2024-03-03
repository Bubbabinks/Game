package game.inventory;

import main.FileManager;
import main.ImageUtil;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public abstract class Inventory implements Serializable {

    protected InventorySlot[] slots;
    private final int width, height;

    private boolean isOpen = false;

    public Inventory(int width, int height) {
        slots = new InventorySlot[width*height];
        int hw = width/2;
        int hh = height/2;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                slots[y*width+x] = new InventorySlot(x-hw, y-hh, null);
            }
        }
        this.width = width;
        this.height = height;
    }

    public ItemStack getItemStack(int x, int y) {
        return slots[y*width+x].getItemStack();
    }

    public boolean addItemStack(ItemStack itemStack) {
        for (int y = 0; y<height; y++) {
            for (int x = 0; x<width; x++) {
                if (slots[y*width+x].getItemStack() == null) {
                    slots[y*width+x].setItemStack(itemStack);
                    return true;
                }else if (slots[y*width+x].getItemStack().getType() == itemStack.getType()) {
                    int remainder = slots[y*width+x].getItemStack().addAmount(itemStack.getAmount());
                    itemStack.setAmount(remainder);
                    if (remainder == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int removeItemAmount(int x, int y, int amount) {
        if (slots[y*width+x].getItemStack() != null) {
            int r = slots[y*width+x].getItemStack().removeAmount(amount);
            if (r == 0) {
                slots[y*width+x].setItemStack(null);
            }
            return r*-1;
        }
        return 0;
    }

    public void removeOne(int x, int y) {
        removeItemAmount(x, y, 1);
    }

    public abstract void draw(Graphics g);

    public void onOpen() {
        isOpen = true;
    }

    public void onClose() {
        isOpen = false;
    }

}
