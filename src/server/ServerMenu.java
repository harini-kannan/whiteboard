package server;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.messaging.*;

// contains the users who are currently in the menu, but not drawing
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
