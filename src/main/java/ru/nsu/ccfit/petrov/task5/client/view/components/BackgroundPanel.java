package ru.nsu.ccfit.petrov.task5.client.view.components;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class BackgroundPanel
    extends JPanel {

    private static final int BORDER_SIZE = 30;
    private final Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;

        setBorder(new EmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
