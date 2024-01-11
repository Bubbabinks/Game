package game.inventory;

import main.FileManager;
import main.ImageUtil;

import java.awt.*;
import java.io.Serializable;

public abstract class Inventory implements Serializable {

    protected static final ImageUtil ITEM_BACKGROUND = new ImageUtil(FileManager.loadInternalImage("inventory/item_background"));
    protected ItemStack[][] items;
    private final int width, height;

    public Inventory(int width, int height) {
        items = new ItemStack[width][height];
        this.width = width;
        this.height = height;
    }

    public ItemStack getItemStack(int x, int y) {
        return items[x][y];
    }

    public boolean addItemStack(ItemStack itemStack) {
        for (int y = 0; y<height; y++) {
            for (int x = 0; x<width; x++) {
                if (items[x][y] == null) {
                    items[x][y] = itemStack;
                    return true;
                }else if (items[x][y].getType() == itemStack.getType()) {
                    int remainder = items[x][y].addAmount(itemStack.getAmount());
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
        if (items[x][y] != null) {
            int r = items[x][y].removeAmount(amount);
            if (r == 0) {
                items[x][y] = null;
            }
            return r*-1;
        }
        return 0;
    }

    public void removeOne(int x, int y) {
        removeItemAmount(x, y, 1);
    }

    public abstract void draw(Graphics g);

}
