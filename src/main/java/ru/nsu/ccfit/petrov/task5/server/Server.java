package ru.nsu.ccfit.petrov.task5.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import ru.nsu.ccfit.petrov.task5.dto.DTOFormat;
import ru.nsu.ccfit.petrov.task5.server.database.UserRepository;
import ru.nsu.ccfit.petrov.task5.server.database.UserRepositoryInMemory;
import ru.nsu.ccfit.petrov.task5.server.service.AcceptService;
import ru.nsu.ccfit.petrov.task5.server.service.RegisterService;
import ru.nsu.ccfit.petrov.task5.server.service.RequestHandleService;

public class Server {

    public void start(InetAddress address, int port, int timeout, DTOFormat dtoFormat)
        throws IOException {
        ServerSocket serverSocket = new ServerSocket(port, 0, address);
        UserRepository userRepository = new UserRepositoryInMemory();
        RequestHandleService requestHandleService = new RequestHandleService(userRepository);
        RegisterService registerService = new RegisterService(userRepository, requestHandleService);
        AcceptService acceptService =  new AcceptService(serverSocket, timeout, dtoFormat, registerService);

        acceptService.start();
    }
}