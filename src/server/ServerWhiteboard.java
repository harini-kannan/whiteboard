package server;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import domain.Drawable;
import domain.Whiteboard;
import server.messaging.*;

/**
 * Wraps the domain Whiteboard with a ClientLobby and ActionQueue
 * 
 * This means that it has clients and can publish messages to them 
 * e.g. "tell all clients to draw __"
 * 
 * This has a threadsafe queue of messages from the MessageBus that it polls on its
 * own thread and responds to (possibly by putting more messages onto the MessageBus
 * directed towards clients).
 */
public class ServerWhiteboard extends ClientLobby implements ActionQueue<ServerWhiteboard> {
    private final Whiteboard whiteboard;

    private final ConcurrentLinkedQueue<Action<ServerWhiteboard>> actions;

    public ServerWhiteboard(Integer id, String name) {
        whiteboard = new Whiteboard(id, name);
        actions = new ConcurrentLinkedQueue<>();
    }
    
    public Integer getId() {
        return whiteboard.getId();
    }
    
    public String getName() {
        return whiteboard.getName();
    }
    
    public void addDrawable(Drawable drawable) {
        whiteboard.addDrawable(drawable);
    }
    
    public List<Drawable> getDrawables() {
        return whiteboard.getDrawables();
    }
    
    public String encode() {
        return String.format("%d-%s", getId(), getName());
    }
    
    // TODO(ddoucet): this is duplicated in ServerMenu... I wonder if I can extract it somehow
    @Override
    public void enqueue(Action<ServerWhiteboard> action) {
        actions.add(action);
    }
    
    public void performActions() {
        Action<ServerWhiteboard> action = null;
        while ((action = actions.poll()) != null)
            action.perform(this);
    }
    
    int pendingActionsCount() {
        return actions.size();
    }
}
