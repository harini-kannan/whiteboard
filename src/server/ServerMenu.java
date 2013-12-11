package server;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.messaging.*;

/**
 * Wraps the domain list of whiteboards with a ClientLobby and ActionQueue
 * 
 * This means that it has clients and can publish messages to them 
 * e.g. "tell all clients that board ____ has been created"
 * 
 * Note that the lobby represents clients who are currently in the menu, but not drawing
 */
public class ServerMenu extends ClientLobby implements ActionQueue<ServerMenu> {    
    private final List<ServerWhiteboard> whiteboards;
    private final ConcurrentLinkedQueue<Action<ServerMenu>> actions;
    
    public ServerMenu() {
        whiteboards = new ArrayList<>();
        actions = new ConcurrentLinkedQueue<>();
    }
    
    public void addWhiteboard(ServerWhiteboard whiteboard) {
        whiteboards.add(whiteboard);
    }
    
    public List<ServerWhiteboard> getWhiteboards() {
        return new ArrayList<ServerWhiteboard>(whiteboards);
    }
    
    @Override
    public void enqueue(Action<ServerMenu> action) {
        actions.add(action);
    }
    
    public void performActions() {
        Action<ServerMenu> action = null;
        while ((action = actions.poll()) != null)
            action.perform(this);
    }
    
    int pendingActionsCount() {
        return actions.size();
    }
}
