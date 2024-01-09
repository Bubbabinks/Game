package menu.main;

import game.GameManager;
import game.world.WorldDetails;
import main.FileManager;
import main.ImageUtil;
import ui.CustomButton;
import ui.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WorldMenuPanel extends JPanel {

    private ArrayList<WorldPanel> worldPanels = new ArrayList<WorldPanel>();
    private static WorldMenuPanel worldMenuPanel;
    private Image background = FileManager.loadInternalImage("menu/world/background");
    private ImageUtil title = FileManager.getImage("menu/world/title");

    public WorldMenuPanel() {
        worldMenuPanel = this;
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

        JPanel worldSelectorPanel = new JPanel();
        worldSelectorPanel.setOpaque(false);
        worldSelectorPanel.setLayout(new BorderLayout());
        worldSelectorPanel.setPreferredSize(new Dimension(1000, 750));
        gc.weighty = 0.75;
        gc.gridy = 1;
        add(worldSelectorPanel, gc);

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setLayout(new GridBagLayout());
        footerPanel.setPreferredSize(new Dimension(1000, 100));
        gc.weighty = 0.1;
        gc.gridy = 2;
        add(footerPanel, gc);

        //WorldPanels
        WorldPanel worldPanel = new WorldPanel("Testing!");
        worldSelectorPanel.add(worldPanel, BorderLayout.NORTH);
        worldPanels.add(worldPanel);

        gc = new GridBagConstraints();

        //Buttons
        CustomButton createWorldButton = new CustomButton("menu/button/create_world", new Dimension(120, 100), new Dimension(100, 50));
        createWorldButton.addActionListener(e -> {
            System.out.println("Create World!");
        });
        gc.weightx = 0.12;
        gc.weighty = 1;
        footerPanel.add(createWorldButton, gc);

        CustomButton loadWorldButton = new CustomButton("menu/button/load_world", new Dimension(120, 100), new Dimension(100, 50));
        loadWorldButton.addActionListener(e -> {
            for (var p: worldPanels) {
                if (p.isSelected) {
                    WorldDetails worldDetails = p.worldDetails;
                    GameManager.loadWorld(worldDetails.getName());
                    GameManager.setPanelToRender();
                }
            }
        });
        gc.gridx = 1;
        footerPanel.add(loadWorldButton, gc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(background, 0, 0, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT, null);
        g.drawImage(title.image, title.centerX, 0, title.width, title.height, null);
        super.paintComponent(g);
    }

    public static void unselectAllWorldPanels() {
        for (var p: worldMenuPanel.worldPanels) {
            p.isSelected = false;
        }
    }
}
