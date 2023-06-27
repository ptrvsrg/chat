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

public class Server {

    public void start(InetAddress address, int port, int timeout, DTOFormat dtoFormat)
        throws IOException {
        ServerSocket serverSocket = new ServerSocket(port, 0, address);
        UserRepository userRepository = new UserRepositoryInMemory();
        RequestHandleService requestHandleService = new RequestHandleService(userRepository);
        RegisterService registerService = new RegisterService(userRepository, requestHandleService);
        AcceptService acceptService =  new AcceptService(serverSocket, timeout, dtoFormat, registerService);

        acceptService.accept();
    }
}