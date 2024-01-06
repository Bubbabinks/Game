package main;

import com.sun.tools.javac.Main;
import game.GameManager;
import ui.MainPanel;
import ui.WindowManager;

public class Manager {

    public static boolean applicationRunning = true;

    public static void main(String[] args) {
        FileManager.init();
        WindowManager.init();
        GameManager.init();
    }

    public static void closeApplication() {
        applicationRunning = false;
    }

}
