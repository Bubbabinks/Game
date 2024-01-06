package game;

import game.world.World;
import ui.MainPanel;
import ui.WindowManager;

public class GameManager {

    public static void init() {
        WindowManager.addKeyListener(new KeyManager());
        World.init();

        MainPanel.setPanel(new Render());
    }

}
