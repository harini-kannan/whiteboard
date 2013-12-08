package server;

import java.util.List;

import org.junit.Test;

import server.messaging.*;
import static org.junit.Assert.*;

public class ClientLobbyTests {
    private TestableClientHandler createAndSubscribeClient(MessageBus messageBus, String nickname) {
        TestableClientHandler client = new TestableClientHandler();
        client.setNickname(nickname);
        messageBus.subscribeClient(client);
        return client;
    }
    
    private void assertHasCorrectMessages(List<String> actual, String... expected) {
        String[] actualArray = actual.toArray(new String[0]);
        assertArrayEquals(expected, actualArray);
    }
    
    @Test
    public void publishToNothingDoesntFailTest() {
        ClientLobby clientLobby = new ClientLobby();
        clientLobby.publishToClients(new MessageBus(), "message");
    }
    
    @Test
    public void subscribePublishTest() {
        MessageBus messageBus = new MessageBus();
        
        TestableClientHandler client = createAndSubscribeClient(messageBus, "nickname");
        
        ClientLobby lobby = new ClientLobby();
        lobby.addClient(client.getNickname());
        
        lobby.publishToClients(messageBus, "message");
        
        assertHasCorrectMessages(client.getAllMessages(), "message");
    }
    
    @Test
    public void multipleSubscribeSinglePublishTest() {
        MessageBus messageBus = new MessageBus();
        
        TestableClientHandler client = createAndSubscribeClient(messageBus, "nickname");
        TestableClientHandler client2 = createAndSubscribeClient(messageBus, "nickname2");
        
        ClientLobby lobby = new ClientLobby();
        lobby.addClient(client.getNickname());
        lobby.addClient(client2.getNickname());
        
        lobby.publishToClients(messageBus, "message");
        
        assertHasCorrectMessages(client.getAllMessages(), "message");
        assertHasCorrectMessages(client2.getAllMessages(), "message");
    }
    
    @Test
    public void clientNotSubscribedDoesntReceiveMessageTest() {
        MessageBus messageBus = new MessageBus();
        
        TestableClientHandler client = createAndSubscribeClient(messageBus, "nickname");
        TestableClientHandler client2 = createAndSubscribeClient(messageBus, "nickname2");
        
        ClientLobby lobby = new ClientLobby();
        lobby.addClient(client.getNickname());
        
        lobby.publishToClients(messageBus, "message");
        
        assertHasCorrectMessages(client.getAllMessages(), "message");
        assertEquals(0, client2.getAllMessages().size());
    }
    
    @Test
    public void subscribePublishUnsubscribePublishTest() {
        MessageBus messageBus = new MessageBus();
        
        TestableClientHandler client = createAndSubscribeClient(messageBus, "nickname");
        
        ClientLobby lobby = new ClientLobby();
        lobby.addClient(client.getNickname());
        
        lobby.publishToClients(messageBus, "message");
        
        assertHasCorrectMessages(client.getAllMessages(), "message");
        
        lobby.removeClient(client.getNickname());
        lobby.publishToClients(messageBus, "message2");
        
        assertEquals(0, client.getAllMessages().size());  // we cleared the queue
    } 
}
