package server.clienthandlertests;

import org.junit.Test;

import server.*;
import server.messaging.*;
import server.requesthandlers.*;
import static org.junit.Assert.*;

/**
 * Test the parser for menu requests
 * 
 * We're looking for two things here:
 * 		1) did it parse correctly and (if necessary) send something to the message bus
 * 		2) did it transition correctly into another request handler
 * 
 * Look in MenuRequestHandler for the possible things it can parse. Each
 * is a separate unit test.
 */
public class MenuRequestHandlerTests {
    private MenuRequestHandler createAndSetMenuRequestHandler(MessageBus messageBus, ClientHandler clientHandler) {
        MenuRequestHandler handler = new MenuRequestHandler(messageBus, clientHandler);
        clientHandler.setCurrentRequestHandler(handler);
        return handler;
    }
    
    @Test
    public void menuMakeTest() {
        TestableClientHandler clientHandler = new TestableClientHandler();
        MenuRequestHandler requestHandler = createAndSetMenuRequestHandler(new MessageBus(), clientHandler);
        
        requestHandler.handle("MAKE board");
        
        assert(clientHandler.getCurrentRequestHandler() instanceof DrawingRequestHandler);
    }
    
    @Test
    public void boardCreateNotificationTest() {
        MessageBus messageBus = new MessageBus();
        ServerMenu menu = Utilities.createAndSubscribeMenu(messageBus);
        
        TestableClientHandler clientHandler = Utilities.createAndSubscribeClient(messageBus, "waffles", menu);
        
        MenuRequestHandler requestHandler = createAndSetMenuRequestHandler(messageBus, new TestableClientHandler());
        requestHandler.handle("MAKE board");
        
        menu.performActions();
        
        assertEquals(1, menu.getWhiteboards().size());
        
        Utilities.assertHasCorrectMessages(
            clientHandler.getAllMessages(),
            "NEW " + menu.getWhiteboards().get(0).getId().toString() + "-board");
    }
    
    @Test
    public void joinBadIdTest() {
        MessageBus messageBus = new MessageBus();
        Utilities.createAndSubscribeMenu(messageBus);
        
        TestableClientHandler clientHandler = new TestableClientHandler();
        MenuRequestHandler requestHandler = createAndSetMenuRequestHandler(messageBus, clientHandler);
        
        requestHandler.handle("JOIN 5");
        Utilities.assertHasCorrectMessages(clientHandler.getAllMessages(), "BADID");
        
        requestHandler.handle("JOIN abc");
        Utilities.assertHasCorrectMessages(clientHandler.getAllMessages(), "BADID");
    }
    
    @Test
    public void requestMenuListTest() {
        MessageBus messageBus = new MessageBus();
        
        ServerMenu menu = Utilities.createAndSubscribeMenu(messageBus);
        menu.addWhiteboard(new ServerWhiteboard(5, "board1"));
        menu.addWhiteboard(new ServerWhiteboard(12, "board2"));
        
        TestableClientHandler clientHandler = Utilities.createAndSubscribeClient(messageBus, "waffles", menu);
        createAndSetMenuRequestHandler(messageBus, clientHandler);
        
        menu.performActions();
        
        Utilities.assertHasCorrectMessages(clientHandler.getAllMessages(), "MENU 5-board1 12-board2");
    }
}
