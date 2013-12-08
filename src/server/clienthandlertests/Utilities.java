package server.clienthandlertests;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import server.ClientHandler;
import server.ServerMenu;
import server.messaging.MessageBus;
import server.requesthandlers.LoginRequestHandler;

public class Utilities {
    public static LoginRequestHandler createLoginRequestHandler(MessageBus messageBus, ClientHandler clientHandler) {
        LoginRequestHandler handler = new LoginRequestHandler(messageBus, clientHandler);
        clientHandler.setCurrentRequestHandler(handler);
        
        return handler;
    }
    
    public static void assertHasCorrectMessages(List<String> actual, String... expected) {
        String[] actualArray = actual.toArray(new String[0]);
        assertArrayEquals(expected, actualArray);
    }
    
    public static ServerMenu createAndSubscribeMenu(MessageBus messageBus) {
        ServerMenu menu = new ServerMenu();
        messageBus.subscribeMenu(menu);
        return menu;
    }
    
    public static TestableClientHandler createAndSubscribeClient(MessageBus messageBus, String nickname) {
        TestableClientHandler client = new TestableClientHandler();
        client.setNickname(nickname);
        messageBus.subscribeClient(client);
        return client;
    }

    public static TestableClientHandler createAndSubscribeClient(
            MessageBus messageBus, String nickname, ServerMenu menu) {
        
        TestableClientHandler client = createAndSubscribeClient(messageBus, nickname);
        menu.addClient(client.getNickname());
        return client;
    }
}
