package game;

import game.inventory.ItemType;
import game.world.BlockType;
import game.world.World;
import game.world.worldGenerator.EarthLikeGenerator;
import ui.MainPanel;
import ui.WindowManager;

public class GameManager {

    public static void init() {
        WindowManager.addKeyListener(new KeyManager());
        World.init();

        new Render();
        BlockType.init();
        ItemType.init();
    }

    public static void loadWorld(String name) {
        String worldName = name;
        if (!World.loadWorld(worldName)) {
            World.createWorld(worldName, new EarthLikeGenerator());
        }
    }

    public static void setPanelToRender() {
        MainPanel.setPanel(Render.getPanel());
        WindowManager.requestFocus();
    }

}
