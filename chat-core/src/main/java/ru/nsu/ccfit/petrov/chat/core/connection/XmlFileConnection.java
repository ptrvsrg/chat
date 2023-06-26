package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;
import ru.nsu.ccfit.petrov.chat.core.dto.XmlUtils;

@RequiredArgsConstructor
public class XmlFileConnection
    implements Connection {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    @Override
    public void connect(Socket clientSocket)
        throws IOException {
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public synchronized void send(DTO dto)
        throws IOException {
        if (out == null) {
            throw new IOException("Connection not established");
        }

        String xml;
        try {
            xml = XmlUtils.dtoToXml(dto);
        } catch (JAXBException e) {
            throw new IOException("Invalid DTO format : " + e);
        }

        out.println(xml);
    }

    @Override
    public synchronized DTO receive()
        throws IOException {
        if (in == null) {
            throw new IOException("Connection not established");
        }

        String xml = in.readLine();

        try {
            return XmlUtils.xmlToDto(xml);
        } catch (JAXBException e) {
            throw new IOException("Invalid DTO format : " + e);
        }
    }

    @Override
    public void close()
        throws IOException {
        if (clientSocket != null) {
            clientSocket.close();
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
}