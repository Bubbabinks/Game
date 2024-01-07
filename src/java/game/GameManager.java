package game;

import game.world.World;
import game.world.worldGenerator.EarthLikeGenerator;
import ui.MainPanel;
import ui.WindowManager;

public class GameManager {

    public static void init() {
        WindowManager.addKeyListener(new KeyManager());
        World.init();

        MainPanel.setPanel(new Render());
        String worldName = "Test World!";
        if (!World.loadWorld(worldName)) {
            World.createWorld(worldName, new EarthLikeGenerator());
        }
    }

}
