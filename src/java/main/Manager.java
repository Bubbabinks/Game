package main;

import game.GameManager;
import game.entity.Bunny;
import game.update.Update;
import game.world.World;
import menu.main.MainMenuManager;
import ui.WindowManager;

public class Manager {

    public static boolean applicationRunning = true;
    public static boolean developmentMode = true;

    public static void main(String[] args) {
        Update.init();
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

        new Bunny();

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
