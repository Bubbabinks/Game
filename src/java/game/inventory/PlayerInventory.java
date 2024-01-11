package game.inventory;

import game.Render;
import main.FileManager;
import main.ImageUtil;
import ui.WindowManager;

import java.awt.*;
import java.awt.event.MouseWheelListener;

public class PlayerInventory extends Inventory {

    private static final ImageUtil SELECTED_ITEM_BACKGROUND = new ImageUtil(FileManager.loadInternalImage("inventory/item_selected_background"));
    private int selectedSlot = 0;

    private transient MouseWheelListener mouseWheelListener = e -> {
        if (e.getWheelRotation() > 0) {
            selectedSlot++;
            if (selectedSlot > 8) {
                selectedSlot-=9;
            }
        }else {
            selectedSlot--;
            if (selectedSlot < 0) {
                selectedSlot+=9;
            }
        }
    };

    public PlayerInventory() {
        super(9, 4);
        Render.addMWL(mouseWheelListener);
    }

    public void onPlayerKilled() {
        Render.removeMWL(mouseWheelListener);
    }

    public Point getSelectedSlot() {
        return new Point(selectedSlot, 0);
    }

    @Override
    public void draw(Graphics g) {

    }

    public void drawUpperHotbar(Graphics g) {
        for (int i=0; i<9; i++) {
            if (i == selectedSlot) {
                g.drawImage(SELECTED_ITEM_BACKGROUND.image, Render.hsw+(SELECTED_ITEM_BACKGROUND.width*i-(SELECTED_ITEM_BACKGROUND.width*9/2)), 0, SELECTED_ITEM_BACKGROUND.width, SELECTED_ITEM_BACKGROUND.height, null);
            }else {
                g.drawImage(ITEM_BACKGROUND.image, Render.hsw+(ITEM_BACKGROUND.width*i-(ITEM_BACKGROUND.width*9/2)), 0, ITEM_BACKGROUND.width, ITEM_BACKGROUND.height, null);
            }
            ItemStack itemStack = items[i][0];
            if (itemStack != null) {
                g.drawImage(itemStack.getType().getImage(), Render.hsw+(ITEM_BACKGROUND.width*i-(ITEM_BACKGROUND.width*9/2))+10, 10, 40, 40, null);
                g.drawString(itemStack.getAmount()+"",Render.hsw+(ITEM_BACKGROUND.width*i-(ITEM_BACKGROUND.width*9/2))+14, 14);
            }


        }
    }

    public void drawLowerHotBar(Graphics g) {
        for (int i=0; i<9; i++) {
            if (i == selectedSlot) {
                g.drawImage(SELECTED_ITEM_BACKGROUND.image, Render.hsw+(SELECTED_ITEM_BACKGROUND.width*i-(SELECTED_ITEM_BACKGROUND.width*9/2)), WindowManager.WINDOW_HEIGHT-SELECTED_ITEM_BACKGROUND.height, SELECTED_ITEM_BACKGROUND.width, SELECTED_ITEM_BACKGROUND.height, null);
            }else {
                g.drawImage(ITEM_BACKGROUND.image, Render.hsw+(ITEM_BACKGROUND.width*i-(ITEM_BACKGROUND.width*9/2)), WindowManager.WINDOW_HEIGHT-ITEM_BACKGROUND.height, ITEM_BACKGROUND.width, ITEM_BACKGROUND.height, null);
            }
            ItemStack itemStack = items[i][0];
            if (itemStack != null) {
                g.drawImage(itemStack.getType().getImage(), Render.hsw+(ITEM_BACKGROUND.width*i-(ITEM_BACKGROUND.width*9/2))+10, WindowManager.WINDOW_HEIGHT-ITEM_BACKGROUND.height+10, 40, 40, null);
                g.drawString(itemStack.getAmount()+"",Render.hsw+(ITEM_BACKGROUND.width*i-(ITEM_BACKGROUND.width*9/2))+14, WindowManager.WINDOW_HEIGHT-ITEM_BACKGROUND.height+24);
            }

        }
    }
}
