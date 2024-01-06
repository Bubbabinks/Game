package ui;

import main.Manager;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowManager {

    public static final int WINDOW_WIDTH = 1000, WINDOW_HEIGHT = 1000;

    private static JFrame frame;

    public static void init() {
        frame = new JFrame("Game");
        frame.setResizable(false);
        frame.setContentPane(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                Manager.closeApplication();
            }
        });

        frame.setVisible(true);
    }

    public static void addKeyListener(KeyListener keyListener) {
        frame.addKeyListener(keyListener);
    }

}
