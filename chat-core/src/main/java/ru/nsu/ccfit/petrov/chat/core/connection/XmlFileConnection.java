package ru.nsu.ccfit.petrov.chat.core.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.chat.core.dto.DTO;
import ru.nsu.ccfit.petrov.chat.core.dto.XmlUtils;

/**
 * The type XmlFileConnection is class that implements interface {@link Connection} for
 * sending/receiving XML files.
 *
 * @author ptrvsrg
 */
@RequiredArgsConstructor
public class XmlFileConnection
    implements Connection {

    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;

    @Override
    public Socket getSocket() {
        return clientSocket;
    }

    @Override
    public void connect(Socket clientSocket)
        throws IOException {
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    @Override
    public void send(DTO dto)
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

        synchronized (out) {
            out.write(xml);
            out.newLine();
            out.flush();
        }
    }

    @Override
    public DTO receive()
        throws IOException {
        if (in == null) {
            throw new IOException("Connection not established");
        }

        String xml = readXml();

        try {
            return XmlUtils.xmlToDto(xml);
        } catch (JAXBException e) {
            throw new IOException("Invalid DTO format : " + e);
        }
    }

    private String readXml()
        throws IOException {
        StringBuilder xmlFile = new StringBuilder();

        synchronized (in) {
            while (true) {
                String line = in.readLine();
                if (line == null || line.equals("")) {
                    break;
                }

                xmlFile.append(line);
                xmlFile.append('\n');
            }
        }

        return xmlFile.toString();
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