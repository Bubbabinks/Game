package main;

import com.sun.tools.javac.Main;
import game.GameManager;
import game.world.World;
import menu.main.MainMenuManager;
import ui.MainPanel;
import ui.WindowManager;

public class Manager {

    public static boolean applicationRunning = true;
    public static boolean developmentMode = true;

    public static void main(String[] args) {
        FileManager.init();
        WindowManager.init();

        GameManager.init();
        if (!developmentMode) {
            MainMenuManager.init();

            MainMenuManager.setPanelToMainMenu();
        }else {
            GameManager.loadWorld("Testing!");
            GameManager.setPanelToRender();
        }

    }

    public static void closeApplication() {
        applicationRunning = false;
        World.save();
        WindowManager.closeApplication();
        if (developmentMode) {
            FileManager.delete(FileManager.mainFolder+"Worlds");
        }
    }

}
