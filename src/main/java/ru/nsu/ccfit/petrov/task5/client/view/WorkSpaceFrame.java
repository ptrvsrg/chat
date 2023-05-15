package ru.nsu.ccfit.petrov.task5.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.client.model.Client;
import ru.nsu.ccfit.petrov.task5.client.view.components.UsersDialog;
import ru.nsu.ccfit.petrov.task5.client.listener.Listener;
import ru.nsu.ccfit.petrov.task5.client.listener.event.ClientErrorEvent;
import ru.nsu.ccfit.petrov.task5.client.listener.event.Event;
import ru.nsu.ccfit.petrov.task5.client.listener.event.LoginEvent;
import ru.nsu.ccfit.petrov.task5.client.listener.event.LogoutEvent;
import ru.nsu.ccfit.petrov.task5.client.listener.event.NewMessageEvent;

public class WorkSpaceFrame
    implements Listener {

    private static final String TITLE = "NSU Chat";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static final int FONT_SIZE = 16;
    private static final int BORDER_INSET = 10;
    private static final String USERS_BUTTON_TEXT = "Users";
    private static final String SEND_BUTTON_TEXT = "Send";
    private static final String USERNAME_MESSAGE = "Username: ";
    private static final String USERNAME_TITLE = "Enter username";
    private final JFrame frame = new JFrame();
    private final Client client = new Client();
    private final JTextArea chatArea = new JTextArea();
    private boolean disposing = false;

    public WorkSpaceFrame() {
        initFrame();
        frame.setVisible(true);

        client.addListener(this);
        client.start();
        if (!disposing) {
            registerUser();
        }
    }

    private void initFrame() {
        frame.setTitle(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowClosingListener(frame));

        frame.getContentPane().setBackground(Color.WHITE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(createChatScrollArea(), BorderLayout.CENTER);
        frame.getContentPane().add(createToolPanel(), BorderLayout.SOUTH);
    }

    private JPanel createToolPanel() {
        JTextArea messageArea = new JTextArea();
        messageArea.setBorder(
            new EmptyBorder(BORDER_INSET, BORDER_INSET, BORDER_INSET, BORDER_INSET));
        messageArea.setFont(new Font(Font.DIALOG, Font.PLAIN, FONT_SIZE));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JButton sendButton = new JButton(SEND_BUTTON_TEXT);
        sendButton.addActionListener(e -> {
            String messageContent = messageArea.getText();
            if (messageContent == null || messageContent.isEmpty()) {
                return;
            }

            messageArea.setText("");
            client.sendUserMessage(messageContent);
        });

        JButton usersButton = new JButton(USERS_BUTTON_TEXT);
        usersButton.addActionListener(e -> {
            Set<String> users = client.getUsers();
            SwingUtilities.invokeLater(() -> new UsersDialog(frame, users));
        });

        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonGroup.add(sendButton);
        buttonGroup.add(usersButton);

        JPanel toolPanel = new JPanel(new BorderLayout());
        toolPanel.add(new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        toolPanel.add(buttonGroup, BorderLayout.EAST);

        return toolPanel;
    }

    private JScrollPane createChatScrollArea() {
        chatArea.setFont(new Font(Font.DIALOG, Font.PLAIN, FONT_SIZE));
        chatArea.setBorder(new EmptyBorder(BORDER_INSET, BORDER_INSET, BORDER_INSET, BORDER_INSET));
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        return new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void registerUser() {
        while (!disposing) {
            String userName = JOptionPane.showInputDialog(frame, USERNAME_MESSAGE, USERNAME_TITLE,
                                                          JOptionPane.INFORMATION_MESSAGE);
            if (userName == null) {
                dispose();
                return;
            }

            if (client.login(userName)) {
                break;
            }
        }
    }

    private void createErrorPane(String reason) {
        JOptionPane.showMessageDialog(frame, reason, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private synchronized void dispose() {
        if (!disposing) {
            disposing = true;
            frame.dispose();
            SwingUtilities.invokeLater(StartMenuFrame::new);
        }
    }

    @Override
    public void processEvent(Event event) {
        if (event instanceof ClientErrorEvent) {
            ClientErrorEvent clientErrorEvent = (ClientErrorEvent) event;
            String reason = clientErrorEvent.getReason();
            boolean terminated = clientErrorEvent.isTerminated();

            createErrorPane(reason);
            if (terminated) {
                dispose();
            }
        } else if (event instanceof LoginEvent) {
            LoginEvent loginEvent = (LoginEvent) event;
            String userName = loginEvent.getUserName();
            String eventMessage = String.format("%s has joined the chat%n", userName);

            chatArea.append(eventMessage);
            chatArea.repaint();
        } else if (event instanceof NewMessageEvent) {
            NewMessageEvent newMessageEvent = (NewMessageEvent) event;
            String userName = newMessageEvent.getUserName();
            String messageContent = newMessageEvent.getMessageContent();
            String eventMessage = String.format("%s : %s%n", userName, messageContent);

            chatArea.append(eventMessage);
            chatArea.repaint();
        } else if (event instanceof LogoutEvent) {
            LogoutEvent logoutEvent = (LogoutEvent) event;
            String userName = logoutEvent.getUserName();
            String eventMessage = String.format("%s has left the chat%n", userName);

            chatArea.append(eventMessage);
            chatArea.repaint();
        }
    }

    @RequiredArgsConstructor
    public class WindowClosingListener
        extends WindowAdapter {

        private static final String EXIT_CONFIRM_TITLE = "Confirmation";
        private static final String EXIT_CONFIRM_MESSAGE = "Are you sure?";
        private final JFrame owner;

        @Override
        public void windowClosing(WindowEvent e) {
            int res = JOptionPane.showConfirmDialog(owner, EXIT_CONFIRM_MESSAGE, EXIT_CONFIRM_TITLE,
                                                    JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                client.logout();
                owner.dispose();
                System.exit(0);
            }
        }
    }
}

