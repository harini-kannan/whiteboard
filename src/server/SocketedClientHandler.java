package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.requesthandlers.*;
import server.logging.ThreadsafeLogger;
import server.messaging.*;

/**
 * Extends a ClientHandler (and thus has a nickname/parsing state machine) but
 * also runs a socket and flushes the message queue to the socket.
 * 
 * This is the main class that the server should be using. It implements runnable
 * which means it can be run on its own thread. 
 */
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
    
    /**
     * Whether there's something to be written or read
     */
    private boolean hasActionReady(BufferedReader in) throws IOException {
        return !messages.isEmpty() || in.ready();
    }
    
    /**
     * Flush the message queue to an output stream
     */
    private void writeAllMessages(PrintWriter out) {
        String message = null;
        while ((message = messages.poll()) != null) {
        	log("<SOCK> Sending: " + message);
            out.println(message);
        }
    }
    
    /**
     * Read until the end of the stream and handle each message as necessary
     */
    private void handleAllMessages(BufferedReader in) throws IOException {
        while (in.ready()) {
            String message = in.readLine().toLowerCase();
            
            log("<SOCK> Received: " + message);
            
            if (message.equals("bye")) {
            	if (getNickname() != null)
            		messageBus.unsubscribeClient(this);
            	
                shouldExit = true;
                return;
            }
            
            currentHandler.handle(message);
        } 
    }
    
    /**
     * Read and write messages until "BYE" is received from the client
     * and make sure to close the socket (and all streams) when leaving.
     */
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
            logger.handleException(clientId.toString(), e, "closing socket's buffered reader");
        }
    }
    
    private void tryCloseSocket(Socket socket) {
        try {
            socket.close();
        }
        catch(IOException e) {
            logger.handleException(clientId.toString(), e, "closing client socket");
        }
    }
}
