package server;

import org.junit.Test;

import server.messaging.*;
import static org.junit.Assert.*;

// This is in server and not server.messaging so that it can access
// public private methods on the server state (namely pendingMessages/ActionCount)
public class MessageBusTests {
    // I wish I could do it as in C#, e.g. createNoOp<ServerMenu>()
    // this'll have to do, instead
    private <T extends ActionQueue<?>> Action<T> createNoOpForType(T t) {
        return new Action<T>() {
            @Override
            public void perform(T t) { }
        };
    }
    
    @Test
    public void menuPublishTest() {
        MessageBus messageBus = new MessageBus();
        
        ServerMenu menu = new ServerMenu();
        messageBus.subscribeMenu(menu);
        
        messageBus.publishToMenu(createNoOpForType(menu));
        assertEquals(menu.pendingActionsCount(), 1);
    }
    
    @Test
    public void whiteboardPublishTest() {
        MessageBus messageBus = new MessageBus();
        
        ServerWhiteboard whiteboard = new ServerWhiteboard(1, "name");
        messageBus.subscribeWhiteboard(whiteboard);
        
        messageBus.publishToWhiteboard(1, createNoOpForType(whiteboard));
        assertEquals(whiteboard.pendingActionsCount(), 1);
    }
    
    @Test
    public void whiteboardPublishDifferentIdDoesntQueueTest() {
        MessageBus messageBus = new MessageBus();
        
        ServerWhiteboard whiteboard = new ServerWhiteboard(1, "board");
        messageBus.subscribeWhiteboard(whiteboard);
        
        ServerWhiteboard whiteboard2 = new ServerWhiteboard(2, "board2");
        messageBus.subscribeWhiteboard(whiteboard2);
        
        messageBus.publishToWhiteboard(2, createNoOpForType(whiteboard));
        assertEquals(whiteboard.pendingActionsCount(), 0);
        assertEquals(whiteboard2.pendingActionsCount(), 1);
    }
    
    @Test
    public void publishDifferentTypeDoesntQueueTest() {
        MessageBus messageBus = new MessageBus();
        
        ServerWhiteboard whiteboard = new ServerWhiteboard(1, "board");
        messageBus.subscribeWhiteboard(whiteboard);
        
        messageBus.publishToMenu(createNoOpForType(new ServerMenu()));
        assertEquals(whiteboard.pendingActionsCount(), 0);
    }
    
    @Test
    public void hasClientKeyTest() {
        MessageBus messageBus = new MessageBus();
        
        ClientHandler clientHandler = new ClientHandler(messageBus, null);  // no socket
        clientHandler.setNickname("nickname");
        
        messageBus.subscribeClient(clientHandler);
        
        assertTrue(messageBus.hasClientNickname("nickname"));
    }
    
    @Test
    public void hasWhiteboardKeyTest() {
        MessageBus messageBus = new MessageBus();
        
        ServerWhiteboard whiteboard = new ServerWhiteboard(1, "name");
        
        messageBus.subscribeWhiteboard(whiteboard);
        
        assertTrue(messageBus.hasWhiteboardId(1));
        assertFalse(messageBus.hasWhiteboardId(2));
    }
}
