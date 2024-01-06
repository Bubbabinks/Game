package ui;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    private static MainPanel mainPanel;

    protected MainPanel() {
        mainPanel = this;
        setPreferredSize(new Dimension(WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT));
        setLayout(new BorderLayout());
    }

    public static void setPanel(JPanel panel) {
        if (mainPanel == null) {
            return;
        }
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.repaint();
    }

}
