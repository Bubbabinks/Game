package main;

import com.sun.tools.javac.Main;
import game.GameManager;
import game.world.World;
import menu.main.MainMenuManager;
import ui.MainPanel;
import ui.WindowManager;

public class Manager {

    public static boolean applicationRunning = true;

    public static void main(String[] args) {
        FileManager.init();
        WindowManager.init();

        MainMenuManager.init();
        GameManager.init();

        MainMenuManager.setPanelToMainMenu();
    }

    public static void closeApplication() {
        applicationRunning = false;
        World.save();
        WindowManager.closeApplication();
    }

}
