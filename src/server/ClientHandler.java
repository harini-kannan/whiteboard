package server;

import java.util.concurrent.ConcurrentLinkedQueue;

import server.logging.ThreadsafeLogger;
import server.messaging.Action;
import server.messaging.ActionQueue;
import server.requesthandlers.RequestHandler;

/**
 * Handles clients. Has a few important things:
 * 		Threadsafe queue of messages to be sent
 * 		Current request parser in the parsing state machine (see RequestHandler)
 * 
 * We also have a threadsafe logger since this will be on the server. Logging is
 * definitely nice to have, but separate threads can step on each other's toes
 * when writing.
 * 
 * This implements ActionQueue so that it can subscribe to and have messages
 * published from the message bus. 
 */
public class ClientHandler implements ActionQueue<ClientHandler> {
    private String nickname;
    
    protected final Integer clientId;
    protected final ThreadsafeLogger logger;
    protected final ConcurrentLinkedQueue<String> messages;
    protected RequestHandler currentHandler;
    
    public ClientHandler(Integer clientId, ThreadsafeLogger logger) {
        this.clientId = clientId;
        this.logger = logger;
        messages = new ConcurrentLinkedQueue<>();
    }
    
    public void log(String string) {
        if (logger != null)
            logger.writeLine(clientId.toString(), string);
    }
    
    @Override
    public void enqueue(Action<ClientHandler> action) {
        action.perform(this);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void setCurrentRequestHandler(RequestHandler to) {
        if (currentHandler != null)
            currentHandler.onLeave();
        
        currentHandler = to;
        to.onEnter();
    }
}
