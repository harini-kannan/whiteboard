package server.messaging;

import java.util.HashMap;
import java.util.Map;

import server.ClientHandler;
import server.ServerMenu;
import server.ServerWhiteboard;
import server.ServerWhiteboardFactory;

/**
 * Threadsafely passes messages (in the form of Actions) to different types.
 * 
 * The code is a little bloated in order to restrict the types of objects
 * that can subscribe and publish but it works. 
 */
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
    
    public void subscribeWhiteboard(ServerWhiteboard whiteboard) {
        synchronized(whiteboards) {
            if (!whiteboards.containsKey(whiteboard.getId()))
                whiteboards.put(whiteboard.getId(), new ActionQueueList<ServerWhiteboard>());
            
            whiteboards.get(whiteboard.getId()).subscribe(whiteboard);
        }
    }
    
    public void publishToWhiteboard(Integer whiteboardId, Action<ServerWhiteboard> action) {
        synchronized(whiteboards) {
            ActionQueueList<ServerWhiteboard> whiteboardList = whiteboards.get(whiteboardId);
            
            if (whiteboardList != null)
                whiteboardList.publish(action);
        }
    }
    
    public boolean hasClientNickname(String nickname) {
        synchronized(clients) {
            return clients.containsKey(nickname);
        }
    }
    
    public void unsubscribeClient(ClientHandler handler) {
    	synchronized(clients) {
    		if (!clients.containsKey(handler.getNickname()))
    			return;
    		
    		ActionQueueList<ClientHandler> list = clients.get(handler.getNickname());
    		list.unsubscribe(handler);
    		
    		if (list.size() == 0)
    			clients.remove(handler.getNickname());
    	}
    }
    
    public void subscribeClient(ClientHandler client) {
        synchronized(clients) {
            if (!clients.containsKey(client.getNickname()))
                clients.put(client.getNickname(), new ActionQueueList<ClientHandler>());
            
            clients.get(client.getNickname()).subscribe(client);
        }
    }
    
    public void publishToClient(String clientNickname, String message) {
        synchronized(clients) {
            ActionQueueList<ClientHandler> clientList = clients.get(clientNickname);
            
            if (clientList != null)
                clientList.publish(new ClientMessageAction(message));
        }
    }
}
