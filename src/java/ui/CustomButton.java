package ui;

import main.FileManager;
import main.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class CustomButton extends JPanel {

    private JButton button;

    public CustomButton(String imagePath, Dimension panelDimension, Dimension buttonDimension) {
        //panel settings
        setOpaque(false);
        setLayout(new GridBagLayout());
        setPreferredSize(panelDimension);

        GridBagConstraints gc = new GridBagConstraints();

        //button
        button = new JButton();
        ImageUtil image = FileManager.getImage(imagePath);
        image.setSubDimension(buttonDimension);
        button.setIcon(new ImageIcon(image.getSubImage(0,0)));
        button.setPressedIcon(new ImageIcon(image.getSubImage(1,0)));
        button.setDisabledIcon(new ImageIcon(image.getSubImage(0,1)));
        button.setDisabledSelectedIcon(new ImageIcon(image.getSubImage(0,1)));
        button.setRolloverIcon(new ImageIcon(image.getSubImage(0,2)));
        button.setRolloverSelectedIcon(new ImageIcon(image.getSubImage(0,2)));
        button.setSelectedIcon(new ImageIcon(image.getSubImage(1,1)));
        button.setBorder(null);

        button.setPreferredSize(buttonDimension);
        add(button, gc);
    }

    public void addActionListener(ActionListener actionListener) {
        button.addActionListener(actionListener);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        button.setEnabled(false);
    }
}
