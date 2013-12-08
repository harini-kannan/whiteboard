package server;

import java.util.concurrent.ConcurrentLinkedQueue;

import server.messaging.Action;
import server.messaging.ActionQueue;
import server.requesthandlers.RequestHandler;

public class ClientHandler implements ActionQueue<ClientHandler> {
    private String nickname;
    
    protected ConcurrentLinkedQueue<String> messages;
    protected RequestHandler currentHandler;
    
    public ClientHandler() {
        messages = new ConcurrentLinkedQueue<>();
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
