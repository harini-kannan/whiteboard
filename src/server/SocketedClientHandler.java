package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.requesthandlers.*;
import server.logging.ThreadsafeLogger;
import server.messaging.*;

public class SocketedClientHandler extends ClientHandler implements Runnable {
    private final Socket socket;
    private final MessageBus messageBus;
    private boolean shouldExit;
    
    public SocketedClientHandler(Integer clientId, ThreadsafeLogger logger, MessageBus messageBus, Socket socket) {
        super(clientId, logger);
        
        this.shouldExit = false;
        this.messageBus = messageBus;
        this.socket = socket;
        this.currentHandler = new LoginRequestHandler(messageBus, this);
    }
    
    private boolean hasActionReady(BufferedReader in) throws IOException {
        return !messages.isEmpty() || in.ready();
    }
    
    private void writeAllMessages(PrintWriter out) {
        String message = null;
        while ((message = messages.poll()) != null)
            out.println(message);
    }
    
    private void handleAllMessages(BufferedReader in) throws IOException {
        while (in.ready()) {
            String message = in.readLine();
            
            log("Received message: " + message.toLowerCase());
            
            if (message.toLowerCase().equals("bye") && getNickname() != null) {
        		messageBus.unsubscribeClient(this);
                shouldExit = true;
                return;
            }
            
            currentHandler.handle(message);
        } 
    }
    
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            while (!shouldExit) {
                if (!hasActionReady(in))
                    Thread.sleep(50); //nothing to do

                writeAllMessages(out);
                handleAllMessages(in);
            }
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
        finally {
        	currentHandler.onLeave();
        	
            if (out != null)
                out.close();
            
            if (in != null)
                tryCloseBufferedReader(in);
            
            tryCloseSocket(socket);
        }
    }
    
 // why do I have to catch this...
    private void tryCloseBufferedReader(BufferedReader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            //logger.handleException(clientId, e, "closing socket's buffered reader");
        }
    }
    
    private void tryCloseSocket(Socket socket) {
        try {
            socket.close();
        }
        catch(IOException e) {
            //logger.handleException(clientId, e, "closing client socket");
        }
    }
}
