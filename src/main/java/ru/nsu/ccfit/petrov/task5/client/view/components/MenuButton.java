package ru.nsu.ccfit.petrov.task5.client.view.components;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

public class MenuButton
    extends JButton {

    private static final int FONT_SIZE = 40;

    public MenuButton(String text) {
        setText(text);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font(Font.DIALOG, Font.PLAIN, FONT_SIZE));
        setForeground(Color.BLACK);
    }
}
