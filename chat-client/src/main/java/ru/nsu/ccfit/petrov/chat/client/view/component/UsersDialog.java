package ru.nsu.ccfit.petrov.chat.client.view.component;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class UsersDialog
    extends JDialog {

    private static final String TITLE = "Users";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 500;
    private static final Font USER_ENTRY_FONT = new Font(Font.DIALOG, Font.PLAIN, 24);
    private final String[] users;

    public UsersDialog(JFrame owner, String[] users) {
        super(owner, true);

        this.users = users;

        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        add(new JScrollPane(createUserEntryArea()));
        setVisible(true);
    }

    private JPanel createUserEntryArea() {
        JPanel userEntryArea = new JPanel();

        userEntryArea.setLayout(new BoxLayout(userEntryArea, BoxLayout.Y_AXIS));
        userEntryArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (String user : users) {
            JLabel userEntry = new JLabel(user);
            userEntry.setFont(USER_ENTRY_FONT);
            userEntry.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            userEntryArea.add(userEntry);
        }

        return userEntryArea;
    }
}