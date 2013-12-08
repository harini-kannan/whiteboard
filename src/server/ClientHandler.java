package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.requesthandlers.*;
import server.messaging.*;

public class ClientHandler implements Runnable, ActionQueue<ClientHandler> {
    private final Socket socket;
    
    private String nickname;
    
    private final ConcurrentLinkedQueue<String> messages;
    
    private RequestHandler currentHandler;
    
    public ClientHandler(MessageBus messageBus, Socket socket) {
        this.socket = socket;
        this.messages = new ConcurrentLinkedQueue<>(); 
        
        this.currentHandler = new LoginRequestHandler(messageBus, this);
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public RequestHandler getCurrentRequestHandler() {
        return currentHandler;
    }
    
    public void setCurrentRequestHandler(RequestHandler to) {
        currentHandler.onLeave();
        
        currentHandler = to;
        currentHandler.onEnter();
    }
    
    public void addMessage(String message) {
        messages.add(message);
    }
    
    // this is used for testing and thus package-private.
    // it isn't useful to have as a public method, also see notes on 
    // ConcurrentLinkedQueue.size()
    int pendingMessageCount() {
        return messages.size();
    }

    @Override
    public void enqueue(Action<ClientHandler> action) {
        // the only action should be addMessage, which is threadsafe
        action.perform(this);
    }
    
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            while (true) {
                if (messages.isEmpty() && !in.ready())
                    Thread.sleep(50); //nothing to do

                // TODO(ddoucet): yuck yuck yuck
                String message = null;
                while ((message = messages.poll()) != null)
                    out.println(message);
                
                while (in.ready()) {
                    message = in.readLine();
                    
                    if (message.toLowerCase().equals("bye"))
                        return;
                    
                    currentHandler.handle(message);
                } 
            }
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
        finally {
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
