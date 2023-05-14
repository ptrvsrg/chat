package ru.nsu.ccfit.petrov.task5.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.client.Client;
import ru.nsu.ccfit.petrov.task5.client.view.components.UsersDialog;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.listener.event.ClientErrorEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.Event;
import ru.nsu.ccfit.petrov.task5.listener.event.LoginEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.LogoutEvent;
import ru.nsu.ccfit.petrov.task5.listener.event.NewMessageEvent;

public class WorkSpaceFrame
    implements Listener {

    private static final String TITLE = "NSU Chat";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static final String USERS_BUTTON_TEXT = "Users";
    private static final String SEND_BUTTON_TEXT = "Send";
    private static final String USERNAME_MESSAGE = "Username: ";
    private static final String USERNAME_TITLE = "Enter username";
    private final JFrame frame = new JFrame();
    private final Client client = new Client();
    private final JTextArea chatArea = new JTextArea();

    public WorkSpaceFrame() {
        initFrame();
        frame.setVisible(true);

        client.addListener(this);
        registerUser();
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
            SwingUtilities.invokeLater(() -> {
                new UsersDialog(frame, users);
            });
        });

        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonGroup.add(sendButton);
        buttonGroup.add(usersButton);

        JPanel toolPanel = new JPanel(new BorderLayout());
        toolPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        toolPanel.add(buttonGroup, BorderLayout.EAST);

        return toolPanel;
    }

    private JScrollPane createChatScrollArea() {
        chatArea.setEditable(false);
        return new JScrollPane(chatArea);
    }

    private void registerUser() {
        String userName = getUserName();
        if (userName == null) {
            frame.dispose();
            SwingUtilities.invokeLater(StartMenuFrame::new);
            return;
        }

        client.login(userName);
    }

    private String getUserName() {
        String userName = JOptionPane.showInputDialog(frame, USERNAME_MESSAGE, USERNAME_TITLE,
                                                      JOptionPane.INFORMATION_MESSAGE);

        while (userName != null && userName.isEmpty()) {
            createErrorPane("Username is invalid");
            userName = JOptionPane.showInputDialog(frame, USERNAME_MESSAGE, USERNAME_TITLE,
                                                   JOptionPane.INFORMATION_MESSAGE);
        }

        return userName;
    }

    private void createErrorPane(String reason) {
        JOptionPane.showMessageDialog(frame, reason, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void processEvent(Event event) {
        if (event instanceof ClientErrorEvent) {
            createErrorPane(((ClientErrorEvent) event).getReason());
            frame.dispose();
            SwingUtilities.invokeLater(StartMenuFrame::new);
        } else if (event instanceof LoginEvent) {
            String eventMessage = String.format("User %s has joined the chat%n",
                                                ((LoginEvent) event).getUserName());
            chatArea.append(eventMessage);
        } else if (event instanceof NewMessageEvent) {
            String eventMessage = String.format("%s : %s%n",
                                                ((NewMessageEvent) event).getUserName(),
                                                ((NewMessageEvent) event).getMessageContent());
            chatArea.append(eventMessage);
        } else if (event instanceof LogoutEvent) {
            String eventMessage = String.format("User %s has left the chat%n",
                                                ((LogoutEvent) event).getUserName());
            chatArea.append(eventMessage);
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

