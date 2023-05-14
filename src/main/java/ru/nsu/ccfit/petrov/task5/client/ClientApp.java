package ru.nsu.ccfit.petrov.task5.client;

import javax.swing.SwingUtilities;
import ru.nsu.ccfit.petrov.task5.client.view.StartMenuFrame;

public class ClientApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartMenuFrame::new);
    }
}
