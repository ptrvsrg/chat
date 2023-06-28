package ru.nsu.ccfit.petrov.chat.client;

import javax.swing.SwingUtilities;
import ru.nsu.ccfit.petrov.chat.client.view.StartMenuFrame;

public class ClientApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartMenuFrame::new);
    }
}
