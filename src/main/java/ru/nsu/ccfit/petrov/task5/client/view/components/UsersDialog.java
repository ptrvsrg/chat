package ru.nsu.ccfit.petrov.task5.client.view.components;

import java.awt.Font;
import java.util.Set;
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
    private static final int FONT_SIZE = 20;
    private static final int BORDER_INSET = 10;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 500;
    private final Set<String> users;

    public UsersDialog(JFrame owner, Set<String> users) {
        super(owner, true);

        this.users = users;

        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        add(new JScrollPane(createTextArea()));
        setVisible(true);
    }

    private JPanel createTextArea() {
        JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
        textArea.setBorder(new EmptyBorder(BORDER_INSET, BORDER_INSET, BORDER_INSET, BORDER_INSET));

        for (String user : users) {
            JLabel scoreLine = new JLabel(String.format("%s%n", user));
            scoreLine.setFont(new Font(Font.DIALOG, Font.BOLD, FONT_SIZE));
            textArea.add(scoreLine);
        }

        return textArea;
    }
}
