package ru.nsu.ccfit.petrov.task5.client.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.client.view.components.BackgroundPanel;
import ru.nsu.ccfit.petrov.task5.client.view.components.MenuButton;

public class StartMenuFrame {

    private static final String TITLE = "Welcome To NSU Chat";
    private static final String BACKGROUND_IMAGE_FILE = "start_menu_background.gif";
    private static final String START_BUTTON_TITLE = "Start";
    private static final String ABOUT_BUTTON_TITLE = "About";
    private static final String ABOUT_PANEL_TITLE = "About";
    private static final String ABOUT_PANEL_MESSAGE =
        "Program \"NSU Chat v1.0\"\n" +
        "Designer: ptrvsrg\n" +
        "Developer: ptrvsrg\n" +
        "Source code: https://github.com/ptrvsrg/NSU_OOP_Java/tree/master/Task5";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private final JFrame frame = new JFrame();

    public StartMenuFrame() {
        initFrame();
        frame.setVisible(true);
    }

    private void initFrame() {
        frame.setTitle(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowClosingListener(frame));
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        frame.getContentPane().setBackground(Color.WHITE);
        frame.getContentPane().add(createBackgroundPanel());
        frame.getContentPane().add(createButtonArea());
    }

    private BackgroundPanel createBackgroundPanel() {
        URL url = StartMenuFrame.class.getClassLoader().getResource(BACKGROUND_IMAGE_FILE);
        Image backgroundImage = Toolkit.getDefaultToolkit().getImage(url);
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);

        backgroundPanel.setPreferredSize(
            new Dimension(3 * frame.getWidth() / 4, frame.getHeight()));

        return backgroundPanel;
    }

    private JPanel createButtonArea() {
        JPanel buttonArea = new JPanel();
        buttonArea.setSize(frame.getWidth() / 2, frame.getHeight() / 2);
        buttonArea.setBackground(new Color(0, 0, 0, 0));
        buttonArea.setLayout(new GridLayout(2, 1));
        buttonArea.add(createMenuButton(START_BUTTON_TITLE, e -> {
            SwingUtilities.invokeLater(WorkSpaceFrame::new);
            frame.dispose();
        }));
        buttonArea.add(createMenuButton(ABOUT_BUTTON_TITLE, e -> {
            JOptionPane.showMessageDialog(frame, ABOUT_PANEL_MESSAGE, ABOUT_PANEL_TITLE,
                                          JOptionPane.INFORMATION_MESSAGE);
        }));

        buttonArea.setPreferredSize(new Dimension(frame.getWidth() / 4, frame.getHeight()));

        return buttonArea;
    }

    private MenuButton createMenuButton(String title, ActionListener listener) {
        MenuButton menuButton = new MenuButton(title);
        menuButton.addActionListener(listener);

        return menuButton;
    }

    @RequiredArgsConstructor
    private class WindowClosingListener
        extends WindowAdapter {

        private static final String EXIT_CONFIRM_TITLE = "Confirmation";
        private static final String EXIT_CONFIRM_MESSAGE = "Are you sure?";
        private final JFrame owner;

        @Override
        public void windowClosing(WindowEvent e) {
            int res = JOptionPane.showConfirmDialog(owner, EXIT_CONFIRM_MESSAGE, EXIT_CONFIRM_TITLE,
                                                    JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                owner.dispose();
                System.exit(0);
            }
        }
    }
}

