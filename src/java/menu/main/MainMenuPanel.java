package menu.main;

import main.FileManager;
import main.ImageUtil;
import ui.CustomButton;
import ui.WindowManager;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    private Image background = FileManager.loadInternalImage("menu/main/background");
    private ImageUtil titleImage = FileManager.getImage("menu/main/title");

    public MainMenuPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        //Panels
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(1000, 500));
        gc.weighty = 0.5;
        gc.anchor = GridBagConstraints.NORTH;
        add(titlePanel, gc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(1000, 400));
        gc.weighty = 0.4;
        gc.gridy = 1;
        add(buttonPanel, gc);

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(1000, 100));
        gc.weighty = 0.1;
        gc.gridy = 2;
        add(footerPanel, gc);

        //buttons
        gc = new GridBagConstraints();
        CustomButton singleplayerButton = new CustomButton("menu/button/singleplayer", new Dimension(1000, 60), new Dimension(300, 50));
        singleplayerButton.addActionListener(e -> {
            MainMenuManager.setPanelToWorldMenu();
        });
        gc.weighty = 55d/400d;
        gc.anchor = GridBagConstraints.NORTH;
        buttonPanel.add(singleplayerButton, gc);

        CustomButton settingsButton = new CustomButton("menu/button/settings", new Dimension(1000, 60), new Dimension(300, 50));
        settingsButton.setEnabled(false);
        settingsButton.addActionListener(e -> {
            System.out.println("Settings Coming Soon");
        });
        gc.gridy = 1;
        buttonPanel.add(settingsButton, gc);

        JPanel buttonSpacer = new JPanel();
        buttonSpacer.setOpaque(false);
        buttonSpacer.setPreferredSize(new Dimension(1000, 400-60*2));
        gc.gridy = 2;
        gc.weighty = (400-60d*2d)/400d;
        buttonPanel.add(buttonSpacer, gc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(background, 0, 0, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT, null);
        g.drawImage(titleImage.image, titleImage.centerX, 100, titleImage.width, titleImage.height, null);
        super.paintComponent(g);
    }
}
