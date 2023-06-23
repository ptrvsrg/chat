package ru.nsu.ccfit.petrov.chat.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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

public class WorkSpaceFrame {

    private static final String TITLE = "NSU Chat";
    private static final String SEND_BUTTON_TITLE = "Send";
    private static final String USERS_BUTTON_TITLE = "Users";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static final int FONT_SIZE = 16;
    private static final int BORDER_INSET = 10;
    private static final Font INPUT_AREA_FONT = new Font(Font.DIALOG, Font.PLAIN, FONT_SIZE);
    private static final Font MESSAGE_PANEL_FONT = new Font(Font.DIALOG, Font.PLAIN, FONT_SIZE);
    private static final Color MESSAGE_PANEL_COLOR = new Color(0, 160, 0);
    private final JFrame frame = new JFrame();
    private final JPanel chatArea = new JPanel();
    private final Controller controller;

    public WorkSpaceFrame(Controller controller) {
        frame.setTitle(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(createContentPane());
        frame.setVisible(true);

        this.controller = controller;
        controller.addListener(new WorkSpaceListener());
    }

    private JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Color.WHITE);
        contentPane.add(createToolPanel(), BorderLayout.SOUTH);
        contentPane.add(createChatArea(), BorderLayout.CENTER);

        return contentPane;
    }

    private JPanel createToolPanel() {
        JPanel toolPanel = new JPanel(new BorderLayout());
        toolPanel.add(createInputArea(), BorderLayout.CENTER);
        toolPanel.add(createButtonGroup(), BorderLayout.EAST);

        return toolPanel;
    }

    private JScrollPane createInputArea() {
        JTextArea inputArea = new JTextArea();
        inputArea.setBorder(
            new EmptyBorder(BORDER_INSET, BORDER_INSET, BORDER_INSET, BORDER_INSET));
        inputArea.setFont(INPUT_AREA_FONT);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        return new JScrollPane(inputArea,
                               JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private JPanel createButtonGroup() {
        JButton sendButton = new JButton(SEND_BUTTON_TITLE);
        sendButton.addActionListener(new SendButtonListener());

        JButton usersButton = new JButton(USERS_BUTTON_TITLE);
        usersButton.addActionListener(new UsersButtonListener());

        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonGroup.add(sendButton);
        buttonGroup.add(usersButton);
        return buttonGroup;
    }

    private JScrollPane createChatArea() {

        return new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private JPanel createEventMessagePanel(String username, String message) {
        JPanel eventMessagePanel = new JPanel(new BorderLayout());


        return eventMessagePanel;
    }

    private JPanel createUserMessagePanel(String username, String message) {
        JPanel userMessagePanel = new JPanel(new BorderLayout());


        return userMessagePanel;
    }

    private class SendButtonListener
        implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class UsersButtonListener
        implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

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
                String userName = loginEvent.getUsername();
                String eventMessage = String.format("%s has joined the chat%n", userName);

                chatArea.repaint();
            } else if (event instanceof NewMessageEvent) {
                NewMessageEvent newMessageEvent = (NewMessageEvent) event;
                String userName = newMessageEvent.getUsername();
                String messageContent = newMessageEvent.getMessage();
                String eventMessage = String.format("%s : %s%n", userName, messageContent);

                chatArea.repaint();
            } else if (event instanceof LogoutEvent) {
                LogoutEvent logoutEvent = (LogoutEvent) event;
                String userName = logoutEvent.getUsername();
                String eventMessage = String.format("%s has left the chat%n", userName);

                chatArea.repaint();
            }
        }
    }
}

