package game.inventory;

import game.Render;
import main.FileManager;
import main.ImageUtil;

import java.awt.*;
import java.io.Serializable;

public class InventorySlot implements Serializable {

    public static final ImageUtil SELECTED_ITEM_BACKGROUND = new ImageUtil(FileManager.loadInternalImage("inventory/item_selected_background"));
    public static final ImageUtil UNSELECTED_ITEM_BACKGROUND = new ImageUtil(FileManager.loadInternalImage("inventory/item_background"));

    private int cx, cy, x, y;
    private boolean isSelected = false;
    private ItemStack itemStack;

    public InventorySlot(int x, int y, ItemStack itemStack) {
        this.itemStack = itemStack;
        setCenterX(x);
        setCenterY(y);
    }

    public void draw(Graphics g) {
        Image image = isSelected?SELECTED_ITEM_BACKGROUND.image:UNSELECTED_ITEM_BACKGROUND.image;
        g.drawImage(image, x, y, getWidth(), getHeight(), null);
        if (itemStack != null) {
            g.drawImage(itemStack.getType().getImage(), x+10, y+10, 40, 40, null);
            g.drawString(itemStack.getAmount()+"",x+14, y+24);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isClickInBounds(Point location) {
        return (location.x > x && location.x < x+getWidth() && location.y > y && location.y < y+getHeight());
    }

    public void setCenterX(int x) {
        this.cx = x;
        this.x = Render.hsw + (x*getWidth()) - (getWidth()/2);
    }

    public void setCenterY(int y) {
        this.cy = y;
        this.y = Render.hsh - ((y*getHeight()) + (getHeight()/2));
    }

    public int getWidth() {
        return isSelected?SELECTED_ITEM_BACKGROUND.width:UNSELECTED_ITEM_BACKGROUND.width;
    }

    public int getHeight() {
        return isSelected?SELECTED_ITEM_BACKGROUND.height:UNSELECTED_ITEM_BACKGROUND.height;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        setCenterX(cx);
        setCenterY(cy);
    }

    public void alignNorth() {
        y = cy*getHeight();
    }

    public void alignSouth() {
        y = Render.sh - ((cy*getHeight()) + getHeight());
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

}
