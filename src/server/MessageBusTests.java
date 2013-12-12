package server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import server.clienthandlertests.TestableClientHandler;
import server.messaging.Action;
import server.messaging.ActionQueue;
import server.messaging.MessageBus;

// This is in server and not server.messaging so that it can access
// public private methods on the server state (namely pendingMessages/ActionCount)
/**
 * Test that queues are separate (in other words that adding to one does not
 * send messages to someone else)
 * 
 * Test that messages are received correctly
 * 
 * Test that hasWhiteboardId and hasClientNickname both work as intended.
 */
public class MessageBusTests {

    private <T extends ActionQueue<?>> Action<T> createNoOpForType(T t) {
        return new Action<T>() {
            @Override
            public void perform(T t) {
            }
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

        ClientHandler clientHandler = new TestableClientHandler();
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
