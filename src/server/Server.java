package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.logging.ThreadsafeConsoleLogger;
import server.messaging.MessageBus;

public class Server {
    private final static int PORT = 4444;
    
    private final ServerSocket serverSocket;
    private final ThreadsafeConsoleLogger logger;
    
    private final MessageBus messageBus;
    private final ServerMenu serverMenu;
    
    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);

        logger = new ThreadsafeConsoleLogger();
        
        messageBus = new MessageBus();
        serverMenu = new ServerMenu();
        messageBus.subscribeMenu(serverMenu);
    }
    
    public void serve() throws IOException {
        createServerStateThread();
        
        for (int clientIndex = 0; ; clientIndex++) {
            logger.writeLine("Server", String.format("Waiting for client %d", clientIndex));
            // block until a client connects
            Socket socket = serverSocket.accept();
            
            // child thread is responsible for closing socket on exit
            Thread thread = new Thread(new SocketedClientHandler(clientIndex, logger, messageBus, socket));
            thread.start();
        }
    }
    
    private void createServerStateThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    serverMenu.performActions();
                    
                    for (ServerWhiteboard whiteboard : serverMenu.getWhiteboards())
                        whiteboard.performActions();
                }
            }
        });
        thread.start();
    }
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.serve();
    }
}
