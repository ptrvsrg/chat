package ru.nsu.ccfit.petrov.chat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import ru.nsu.ccfit.petrov.chat.core.dto.DTOFormat;
import ru.nsu.ccfit.petrov.chat.server.database.UserRepository;
import ru.nsu.ccfit.petrov.chat.server.database.UserRepositoryInMemory;
import ru.nsu.ccfit.petrov.chat.server.service.AcceptService;
import ru.nsu.ccfit.petrov.chat.server.service.RegisterService;
import ru.nsu.ccfit.petrov.chat.server.service.RequestHandleService;

/**
 * The type Server is class that instantiates repository and all services and starts accepting,
 * registration and handling clients.
 *
 * @author ptrvsrg
 */
public class Server {

    /**
     * Start server.
     *
     * @param address   the address
     * @param port      the port
     * @param timeout   the timeout
     * @param dtoFormat the DTO format
     * @throws IOException If an I/O error occurs when opening the server socket.
     */
    public void start(InetAddress address, int port, int timeout, DTOFormat dtoFormat)
        throws IOException {
        ServerSocket serverSocket = new ServerSocket(port, 0, address);
        UserRepository userRepository = new UserRepositoryInMemory();
        RequestHandleService requestHandleService = new RequestHandleService(userRepository);
        RegisterService registerService = new RegisterService(userRepository, requestHandleService);
        AcceptService acceptService = new AcceptService(serverSocket, timeout, dtoFormat,
                                                        registerService);

        acceptService.accept();
    }
}