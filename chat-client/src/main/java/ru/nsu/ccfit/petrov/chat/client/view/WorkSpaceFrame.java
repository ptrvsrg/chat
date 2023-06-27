package ru.nsu.ccfit.petrov.chat.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.chat.client.controller.Controller;
import ru.nsu.ccfit.petrov.chat.client.listener.Listener;
import ru.nsu.ccfit.petrov.chat.client.listener.event.ErrorEvent;
import ru.nsu.ccfit.petrov.chat.client.listener.event.Event;
import ru.nsu.ccfit.petrov.chat.client.listener.event.LoginEvent;
import ru.nsu.ccfit.petrov.chat.client.listener.event.LogoutEvent;
import ru.nsu.ccfit.petrov.chat.client.listener.event.NewMessageEvent;
import ru.nsu.ccfit.petrov.chat.client.view.component.UsersDialog;

public class WorkSpaceFrame {

    private static final String TITLE = "NSU Chat";
    private static final String SEND_BUTTON_ICON_FILE = "send_button.png";
    private static final String USERS_BUTTON_ICON_FILE = "users_button.png";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static final int BUTTON_ICON_SIZE = 32;
    private static final Font INPUT_AREA_FONT = new Font(Font.DIALOG, Font.PLAIN, 18);
    private static final Font BUTTON_FONT = new Font(Font.DIALOG, Font.PLAIN, 18);
    private static final Font USERNAME_TITLE_FONT = new Font(Font.DIALOG, Font.BOLD, 20);
    private static final Font MESSAGE_FONT = new Font(Font.DIALOG, Font.PLAIN, 18);
    private static final Font EVENT_FONT = new Font(Font.DIALOG, Font.BOLD, 16);
    private static final Color TOOL_PANEL_COLOR = new Color(0, 150, 150);
    private final JFrame frame = new JFrame();
    private final JPanel chatPanel = new JPanel();
    private final JTextArea inputArea = new JTextArea();
    private final Controller controller;

    public WorkSpaceFrame(Controller controller) {
        frame.setTitle(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(createContentPane());
        frame.setVisible(true);

        this.controller = controller;
        controller.addListener(new WorkSpaceListener());
    }

    private JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(createChatPanel(), BorderLayout.CENTER);
        contentPane.add(createToolPanel(), BorderLayout.SOUTH);
        return contentPane;
    }

    private JScrollPane createChatPanel() {
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);
        chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return new JScrollPane(chatPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void addEventMessage(String message) {
        JLabel eventMessageLabel = new JLabel(String.format("<html>%s</html>", message),
                                              SwingConstants.CENTER);
        eventMessageLabel.setFont(EVENT_FONT);
        eventMessageLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        chatPanel.add(eventMessageLabel);
    }

    private void addUserMessage(String username, String message, boolean isCurrentUser) {
        JLabel usernameLabel = new JLabel(username + (isCurrentUser ? " (You)" : ""), SwingConstants.LEFT);
        usernameLabel.setFont(USERNAME_TITLE_FONT);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        chatPanel.add(usernameLabel);

        JLabel messageLabel = new JLabel();
        messageLabel.setText("<html>" + message.replace("<", "&lt;")
                                               .replace(">", "&gt;")
                                               .replace("\n", "<br/>") + "</html>");
        messageLabel.setFont(MESSAGE_FONT);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        chatPanel.add(messageLabel);
    }

    private JPanel createToolPanel() {
        JPanel toolPanel = new JPanel(new BorderLayout());
        toolPanel.setBackground(TOOL_PANEL_COLOR);
        toolPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        toolPanel.add(createInputArea(), BorderLayout.CENTER);
        toolPanel.add(createButtonGroup(), BorderLayout.EAST);

        return toolPanel;
    }

    private JScrollPane createInputArea() {
        inputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputArea.setFont(INPUT_AREA_FONT);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        return new JScrollPane(inputArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private JPanel createButtonGroup() {
        JButton sendButton = createToolButton(SEND_BUTTON_ICON_FILE, new SendButtonListener());
        JButton usersButton = createToolButton(USERS_BUTTON_ICON_FILE, new UsersButtonListener());

        JPanel buttonGroup = new JPanel(new GridLayout(1, 2));
        buttonGroup.setBackground(TOOL_PANEL_COLOR);
        buttonGroup.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        buttonGroup.add(sendButton);
        buttonGroup.add(usersButton);
        return buttonGroup;
    }

    private static JButton createToolButton(String iconFile, ActionListener listener) {
        URL buttonIconUrl = StartMenuFrame.class.getClassLoader()
                                                .getResource(iconFile);
        Image buttonImage = Toolkit.getDefaultToolkit()
                                   .createImage(buttonIconUrl);
        ImageIcon buttonIcon = new ImageIcon(
            buttonImage.getScaledInstance(BUTTON_ICON_SIZE, BUTTON_ICON_SIZE, Image.SCALE_DEFAULT));

        JButton button = new JButton();
        button.setIcon(buttonIcon);
        button.setFont(BUTTON_FONT);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    private class SendButtonListener
        implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String message = inputArea.getText();
            if (message == null || message.isEmpty()) {
                return;
            }

            inputArea.setText("");
            controller.sendNewMessage(message);
        }
    }

    private class UsersButtonListener
        implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] users = controller.getUsers();
            SwingUtilities.invokeLater(() -> new UsersDialog(frame, users));
        }
    }

    @RequiredArgsConstructor
    private class WorkSpaceListener
        implements Listener {

        @Override
        public void processEvent(Event event) {
            if (event instanceof ErrorEvent) {
                frame.dispose();
                SwingUtilities.invokeLater(StartMenuFrame::new);
            } else if (event instanceof LoginEvent) {
                LoginEvent loginEvent = (LoginEvent) event;
                String eventMessage = String.format("%s has joined the chat%n",
                                                    loginEvent.getUsername());

                addEventMessage(eventMessage);
                chatPanel.repaint();
            } else if (event instanceof NewMessageEvent) {
                NewMessageEvent newMessageEvent = (NewMessageEvent) event;

                addUserMessage(newMessageEvent.getUsername(), newMessageEvent.getMessage(),
                               newMessageEvent.isCurrentUser());
                chatPanel.repaint();
            } else if (event instanceof LogoutEvent) {
                LogoutEvent logoutEvent = (LogoutEvent) event;
                String eventMessage = String.format("%s has left the chat%n",
                                                    logoutEvent.getUsername());

                addEventMessage(eventMessage);
                chatPanel.repaint();
            }
        }
    }
}

