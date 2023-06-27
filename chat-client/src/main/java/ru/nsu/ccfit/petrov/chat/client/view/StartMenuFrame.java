package ru.nsu.ccfit.petrov.chat.client.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import ru.nsu.ccfit.petrov.chat.client.controller.Controller;
import ru.nsu.ccfit.petrov.chat.client.listener.Listener;
import ru.nsu.ccfit.petrov.chat.client.listener.event.ErrorEvent;
import ru.nsu.ccfit.petrov.chat.client.listener.event.Event;

public class StartMenuFrame {

    private static final String TITLE = "Welcome to NSU Chat";
    private static final String SERVER_ADDRESS_FIELD_TITLE = "Server address: ";
    private static final String SERVER_PORT_FIELD_TITLE = "Server port: ";
    private static final String USERNAME_FIELD_TITLE = "Username: ";
    private static final String START_BUTTON_TITLE = "Start";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 800;
    private static final int POPUP_DISPLAY = 5000;
    private static final Color BACKGROUND_COLOR = new Color(0, 150, 150);
    private static final Color BUTTON_COLOR = new Color(0, 140, 140);
    private static final Color ERROR_MESSAGE_COLOR = new Color(0, 150, 150);
    private static final Font TITLE_FONT = new Font(Font.DIALOG, Font.BOLD, 50);
    private static final Font FIELD_TITLE_FONT = new Font(Font.DIALOG, Font.BOLD, 25);
    private static final Font FIELD_TEXT_FONT = new Font(Font.DIALOG, Font.PLAIN, 22);
    private static final Font BUTTON_FONT = new Font(Font.DIALOG, Font.BOLD, 30);
    private static final Font ERROR_MESSAGE_FONT = new Font(Font.DIALOG, Font.BOLD, 25);
    private final JFrame frame = new JFrame();
    private final JTextField serverAddressTextField = new JTextField();
    private final JTextField serverPortTextField = new JTextField();
    private final JTextField usernameTextField = new JTextField();
    private final JLabel errorMessageLabel = new JLabel();
    private final Controller controller = new Controller();
    private final GridBagConstraints contentPaneConstraints = new GridBagConstraints();

    public StartMenuFrame() {
        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(createContentPane());
        frame.setVisible(true);

        controller.addListener(new StartMenuListener());
    }

    private JPanel createContentPane() {
        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(BACKGROUND_COLOR);

        contentPaneConstraints.gridx = GridBagConstraints.RELATIVE;
        contentPaneConstraints.gridy = 0;

        addAboutButton(contentPane);
        addTitleLabel(contentPane);
        addInputPanel(contentPane);
        addStartButton(contentPane);
        addErrorLabel(contentPane);

        return contentPane;
    }

    private void addAboutButton(JPanel contentPane) {
        JButton aboutButton = new JButton("?");
        aboutButton.setFont(BUTTON_FONT);
        aboutButton.setForeground(Color.WHITE);
        aboutButton.setBackground(BUTTON_COLOR);
        aboutButton.addActionListener(new AboutButtonListener());

        contentPaneConstraints.anchor = GridBagConstraints.NORTHWEST;
        contentPaneConstraints.insets = new Insets(0, 0, 50, 0);

        contentPane.add(aboutButton, contentPaneConstraints);

        contentPaneConstraints.gridy++;
    }

    private void addTitleLabel(JPanel contentPane) {
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        contentPaneConstraints.anchor = GridBagConstraints.CENTER;
        contentPaneConstraints.insets = new Insets(50, 0, 50, 0);

        contentPane.add(titleLabel, contentPaneConstraints);

        contentPaneConstraints.gridy++;
    }

    private void addInputPanel(JPanel contentPane) {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        inputPanel.setBackground(new Color(0, 0, 0, 0));

        contentPaneConstraints.anchor = GridBagConstraints.CENTER;
        contentPaneConstraints.insets = new Insets(50, 0, 50, 0);

        contentPane.add(inputPanel, contentPaneConstraints);

        contentPaneConstraints.gridy++;

        addField(SERVER_ADDRESS_FIELD_TITLE, serverAddressTextField, inputPanel);
        addField(SERVER_PORT_FIELD_TITLE, serverPortTextField, inputPanel);
        addField(USERNAME_FIELD_TITLE, usernameTextField, inputPanel);
    }

    private void addField(String fieldTitle, JTextField textField, JPanel inputPanel) {
        JLabel fieldLabel = new JLabel(fieldTitle);
        fieldLabel.setFont(FIELD_TITLE_FONT);
        fieldLabel.setForeground(Color.WHITE);
        inputPanel.add(fieldLabel);

        textField.setFont(FIELD_TEXT_FONT);
        inputPanel.add(textField);
    }

    private void addStartButton(JPanel contentPane) {
        JButton startButton = new JButton(START_BUTTON_TITLE);
        startButton.setFont(BUTTON_FONT);
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(BUTTON_COLOR);
        startButton.addActionListener(new StartButtonListener());

        contentPaneConstraints.anchor = GridBagConstraints.CENTER;
        contentPaneConstraints.insets = new Insets(50, 0, 0, 0);

        contentPane.add(startButton, contentPaneConstraints);

        contentPaneConstraints.gridy++;
    }

    private void addErrorLabel(JPanel contentPane) {
        errorMessageLabel.setFont(ERROR_MESSAGE_FONT);
        errorMessageLabel.setForeground(ERROR_MESSAGE_COLOR);
        errorMessageLabel.setOpaque(false);
        errorMessageLabel.setBackground(Color.WHITE);
        errorMessageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        errorMessageLabel.setText(" ");

        contentPaneConstraints.anchor = GridBagConstraints.CENTER;
        contentPaneConstraints.insets = new Insets(50, 0, 0, 0);

        contentPane.add(errorMessageLabel, contentPaneConstraints);

        contentPaneConstraints.gridy++;
    }

    private void showErrorMessage(String message) {
        errorMessageLabel.setOpaque(true);
        errorMessageLabel.setText(message);

        Timer timer = new Timer(POPUP_DISPLAY, e -> {
            errorMessageLabel.setOpaque(false);
            errorMessageLabel.setText(" ");
        });
        timer.setRepeats(false);
        timer.start();
    }

    private class AboutButtonListener
        implements ActionListener {

        private static final String ABOUT_PANEL_TITLE = "About";
        private static final String ABOUT_PANEL_MESSAGE =
            "NSU Chat v1.0\n" + "Designer: ptrvsrg\n" + "Developer: ptrvsrg\n";

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, ABOUT_PANEL_MESSAGE, ABOUT_PANEL_TITLE,
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class StartButtonListener
        implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String serverAddressText = serverAddressTextField.getText();
            String serverPortText = serverPortTextField.getText();
            String username = usernameTextField.getText();

            if (serverAddressText.isEmpty()) {
                showErrorMessage("Fill in the field \"Server address\"");
                return;
            }
            if (serverPortText.isEmpty()) {
                showErrorMessage("Fill in the field \"Server port\"");
                return;
            }
            if (username.isEmpty()) {
                showErrorMessage("Fill in the field \"Username\"");
                return;
            }

            InetAddress serverAddress;
            try {
                serverAddress = InetAddress.getByName(serverAddressText);
            } catch (UnknownHostException ex) {
                showErrorMessage("IP address of a server could not be determined");
                return;
            }

            int serverPort;
            try {
                serverPort = Integer.parseInt(serverPortText);
            } catch (NumberFormatException ex) {
                showErrorMessage("Port of a server is invalid number");
                return;
            }

            if (!controller.connect(serverAddress, serverPort)) {
                return;
            }
            if (!controller.login(username)) {
                return;
            }

            SwingUtilities.invokeLater(() -> {
                frame.dispose();
                new WorkSpaceFrame(controller);
            });
        }
    }

    private class StartMenuListener
        implements Listener {

        @Override
        public void processEvent(Event event) {
            if (event instanceof ErrorEvent) {
                showErrorMessage(((ErrorEvent) event).getReason());
            }
        }
    }
}