package menu.main;

import main.FileManager;
import ui.WindowManager;

import javax.swing.*;
import java.awt.*;

public class CreateWorldPanel extends JPanel {

    private static Image background = FileManager.loadInternalImage("menu/world/background");

    public CreateWorldPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        //Panels
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(1000, 150));
        gc.weighty = 0.15;
        gc.anchor = GridBagConstraints.NORTH;
        add(titlePanel, gc);

        JPanel optionPanel = new JPanel();
        optionPanel.setOpaque(false);
        optionPanel.setLayout(new BorderLayout());
        optionPanel.setPreferredSize(new Dimension(1000, 750));
        gc.weighty = 0.75;
        gc.gridy = 1;
        add(optionPanel, gc);

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setLayout(new GridBagLayout());
        footerPanel.setPreferredSize(new Dimension(1000, 100));
        gc.weighty = 0.1;
        gc.gridy = 2;
        add(footerPanel, gc);

        //Title

    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(background, 0, 0, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT, null);
        super.paintComponent(g);
    }
}
