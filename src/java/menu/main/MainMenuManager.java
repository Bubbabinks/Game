package menu.main;

import ui.MainPanel;
import ui.WindowManager;

public class MainMenuManager {

    private static MainMenuPanel mainMenuPanel;
    private static WorldMenuPanel worldMenuPanel;

    public static void init() {
        mainMenuPanel = new MainMenuPanel();
        worldMenuPanel = new WorldMenuPanel();

    }

    public static void setPanelToMainMenu() {
        MainPanel.setPanel(mainMenuPanel);
    }
    public static void setPanelToWorldMenu() {
        MainPanel.setPanel(worldMenuPanel);
    }

    public static void repaint() {
        mainMenuPanel.repaint();
        worldMenuPanel.repaint();
    }

}
