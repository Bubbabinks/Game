package menu.main;

import game.world.WorldDetails;
import main.FileManager;
import main.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.zip.GZIPInputStream;

public class WorldPanel extends JPanel {

    public boolean isSelected = false;
    public WorldDetails worldDetails;
    private ImageUtil background = new ImageUtil(FileManager.loadInternalImage("menu/world/world_panel"));
    private Image worldIcon;
    private Image isSelectedBackground, isNotSelectedBackground;

    public WorldPanel(String worldName) {
        worldDetails = FileManager.findWorldDetails(worldName);
        worldIcon = FileManager.loadWorldImage(worldDetails);
        background.setSubImageWidth(800);
        background.setSubDimension(new Dimension(800, 150));
        isSelectedBackground = background.getSubImage(0, 1);
        isNotSelectedBackground = background.getSubImage(0, 0);
        setPreferredSize(new Dimension(1000, 170));
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();

        JPanel contentPanel = new JPanel();
        contentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 1) {
                    WorldMenuPanel.unselectAllWorldPanels();
                    isSelected = true;
                    repaint();
                }
            }
        });
        contentPanel.setPreferredSize(new Dimension(800, 150));
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());
        gc.weightx = .8;
        gc.weighty = .8823;
        gc.insets = new Insets(10, 100, 10, 100);
        add(contentPanel, gc);

        gc = new GridBagConstraints();

        JPanel iconPanel = new JPanel();
        iconPanel.setPreferredSize(new Dimension(150, 150));
        iconPanel.setOpaque(false);
        gc.weightx = 0.1875;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.WEST;
        contentPanel.add(iconPanel, gc);

        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(650, 150));
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setOpaque(false);
        gc.weightx = 0.8125;
        gc.gridx = 1;
        contentPanel.add(infoPanel, gc);

        gc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Name: "+worldName);
        titleLabel.setPreferredSize(new Dimension(620, 50));
        titleLabel.setOpaque(false);
        titleLabel.setForeground(Color.BLACK);
        gc.weightx = 1;
        gc.weighty = 0.1333;
        gc.anchor = GridBagConstraints.NORTHEAST;
        infoPanel.add(titleLabel, gc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected) {
            g.drawImage(isSelectedBackground, 100, 10, 800, 150, null);
        }else {
            g.drawImage(isNotSelectedBackground, 100, 10, 800, 150, null);
        }
        g.drawImage(worldIcon, 125, 35, 100, 100, null);

    }
}
