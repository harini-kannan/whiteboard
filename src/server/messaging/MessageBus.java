package server.messaging;

import java.util.HashMap;
import java.util.Map;

import server.ClientHandler;
import server.ServerMenu;
import server.ServerWhiteboard;
import server.ServerWhiteboardFactory;

public class MessageBus {
    private final ActionQueueList<ServerMenu> menus;
    private final Map<Integer, ActionQueueList<ServerWhiteboard>> whiteboards;
    private final Map<String, ActionQueueList<ClientHandler>> clients;
    private final ServerWhiteboardFactory whiteboardFactory;
    
    public MessageBus() {
        menus = new ActionQueueList<ServerMenu>();
        whiteboards = new HashMap<>();
        clients = new HashMap<>();
        whiteboardFactory = new ServerWhiteboardFactory(this);
    }
    
    // TODO(ddoucet): not sure this really belongs here
    public ServerWhiteboardFactory getWhiteboardFactory() {
        return whiteboardFactory;
    }
    
    public void subscribeMenu(ServerMenu queue) {
        synchronized(menus) {
            menus.subscribe(queue);
        }
    }
    
    public void publishToMenu(Action<ServerMenu> action) {
        synchronized(menus) {
            menus.publish(action);
        }
    }
    
    public boolean hasWhiteboardId(Integer whiteboardId) {
        synchronized(whiteboards) {
            return whiteboards.containsKey(whiteboardId);
        }
    }
    
    public void subscribeToWhiteboard(Integer whiteboardId, ServerWhiteboard queue) {
        synchronized(whiteboards) {
            if (!whiteboards.containsKey(whiteboardId))
                whiteboards.put(whiteboardId, new ActionQueueList<ServerWhiteboard>());
            
            whiteboards.get(whiteboardId).subscribe(queue);
        }
    }
    
    public void publishToWhiteboard(Integer whiteboardId, Action<ServerWhiteboard> action) {
        synchronized(whiteboards) {
            whiteboards.get(whiteboardId).publish(action);
        }
    }
    
    public boolean hasClientNickname(String nickname) {
        synchronized(clients) {
            return clients.containsKey(nickname);
        }
    }
    
    public void subscribeToClient(String clientNickname, ClientHandler queue) {
        synchronized(clients) {
            if (!clients.containsKey(clientNickname))
                clients.put(clientNickname, new ActionQueueList<ClientHandler>());
            
            clients.get(clientNickname).subscribe(queue);
        }
    }
    
    public void publishToClient(String clientNickname, String message) {
        synchronized(clients) {
            clients.get(clientNickname)
                .publish(new ClientMessageAction(message));
        }
    }
}
