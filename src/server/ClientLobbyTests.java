package server;

import org.junit.Test;

import server.messaging.*;
import static org.junit.Assert.*;

public class ClientLobbyTests {
    private ClientHandler createAndSubscribeClient(MessageBus messageBus, String nickname) {
        ClientHandler client = new ClientHandler(messageBus, null);
        client.setNickname(nickname);
        messageBus.subscribeClient(client);
        return client;
    }
    
    @Test
    public void publishToNothingDoesntFailTest() {
        ClientLobby clientLobby = new ClientLobby();
        clientLobby.publishToClients(new MessageBus(), "message");
    }
    
    @Test
    public void subscribePublishTest() {
        MessageBus messageBus = new MessageBus();
        
        ClientHandler client = createAndSubscribeClient(messageBus, "nickname");
        
        ClientLobby lobby = new ClientLobby();
        lobby.addClient(client.getNickname());
        
        lobby.publishToClients(messageBus, "message");
        
        assertEquals(client.pendingMessageCount(), 1);
    }
    
    @Test
    public void multipleSubscribeSinglePublishTest() {
        MessageBus messageBus = new MessageBus();
        
        ClientHandler client = createAndSubscribeClient(messageBus, "nickname");
        ClientHandler client2 = createAndSubscribeClient(messageBus, "nickname2");
        
        ClientLobby lobby = new ClientLobby();
        lobby.addClient(client.getNickname());
        lobby.addClient(client2.getNickname());
        
        lobby.publishToClients(messageBus, "message");
        
        assertEquals(client.pendingMessageCount(), 1);
        assertEquals(client2.pendingMessageCount(), 1);
    }
    
    @Test
    public void clientNotSubscribedDoesntReceiveMessageTest() {
        MessageBus messageBus = new MessageBus();
        
        ClientHandler client = createAndSubscribeClient(messageBus, "nickname");
        ClientHandler client2 = createAndSubscribeClient(messageBus, "nickname2");
        
        ClientLobby lobby = new ClientLobby();
        lobby.addClient(client.getNickname());
        
        lobby.publishToClients(messageBus, "message");
        
        assertEquals(client.pendingMessageCount(), 1);
        assertEquals(client2.pendingMessageCount(), 0);
    }
    
    @Test
    public void subscribePublishUnsubscribePublishTest() {
        MessageBus messageBus = new MessageBus();
        
        ClientHandler client = createAndSubscribeClient(messageBus, "nickname");
        
        ClientLobby lobby = new ClientLobby();
        lobby.addClient(client.getNickname());
        
        lobby.publishToClients(messageBus, "message");
        
        assertEquals(client.pendingMessageCount(), 1);
        
        lobby.removeClient(client.getNickname());
        lobby.publishToClients(messageBus, "message2");
        
        assertEquals(client.pendingMessageCount(), 1);  // still only 1
    } 
}
