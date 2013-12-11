package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.logging.ThreadsafeConsoleLogger;
import server.messaging.MessageBus;

/**
 * Main Server class. We need to do two things:
 * 		1) initialize a thread that simply flushes messages for the menu and whiteboards
 *		2) listen for incoming client requests and spawn a separate thread to handle each one
 */
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
    
    /**
     * Creates the server state thread and listens for clients to connect
     */
    public void serve() throws IOException {
        createServerStateThread();
        
        for (int clientIndex = 0; ; clientIndex++) {
            logger.writeLine("Server", String.format("<SOCK> Waiting for client %d", clientIndex));
            
            // block until a client connects
            Socket socket = serverSocket.accept();
            
            // child thread is responsible for closing socket on exit
            Thread thread = new Thread(new SocketedClientHandler(clientIndex, logger, messageBus, socket));
            thread.start();
        }
    }
    
    /**
     * Creates the thread that checks messages for all Whiteboards and the ServerMenu
     */
    private void createServerStateThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    serverMenu.performActions();
                    
                    for (ServerWhiteboard whiteboard : serverMenu.getWhiteboards())
                        whiteboard.performActions();
                    
                    try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
