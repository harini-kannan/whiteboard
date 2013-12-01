package server;

import java.util.*;

import server.messaging.MessageBus;

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
    
    public void publishToClients(MessageBus messageBus, String message) {
        for (String client : connectedClientUsernames)
            messageBus.publishToClient(client, message);
    }
}
