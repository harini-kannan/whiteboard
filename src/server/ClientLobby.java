package server;

import java.util.*;

import server.messaging.MessageBus;

/**
 * Wraps a list of clients that can be published to (e.g. for menus and whiteboards).
 */
public class ClientLobby {
    protected List<String> connectedClientUsernames;
    
    public ClientLobby() {
        connectedClientUsernames = new ArrayList<>();
    }
    
    public void addClient(String clientNickname) {
        connectedClientUsernames.add(clientNickname);
    }
    
    public void removeClient(String clientNickname) {
        connectedClientUsernames.remove(clientNickname);
    }
    
    public List<String> getClientUsernames() {
        return new ArrayList<>(connectedClientUsernames);
    }
    
    public void publishToClients(MessageBus messageBus, String message) {
        for (String client : connectedClientUsernames)
            messageBus.publishToClient(client, message);
    }
}
