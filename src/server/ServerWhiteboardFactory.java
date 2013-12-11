package server;

import server.messaging.*;

/**
 * Wraps threadsafe whiteboard creation.
 * 
 * A couple things need to happen when whiteboards are created:
 * 		1) it needs a unique id (which is where the threadsafety comes in)
 * 				this is because board names are not necessarily unique
 * 		2) it needs to subscribe to the message bus
 * 		3) it needs to publish to everyone in the menu state that a new board
 * 				is ready to be joined
 */
public class ServerWhiteboardFactory {
    private final MessageBus messageBus;
    private Integer lastUsedId;  // we just increment this each time
    
    public ServerWhiteboardFactory(MessageBus messageBus) {
        this.messageBus = messageBus;
        lastUsedId = 0;
    }
    
    public ServerWhiteboard create(String name) {
        final ServerWhiteboard board = instantiateWhiteboard(name);
        
        messageBus.subscribeWhiteboard(board);
        messageBus.publishToMenu(new Action<ServerMenu>() {
            @Override
            public void perform(ServerMenu menu) {
                menu.addWhiteboard(board);
                menu.publishToClients(messageBus, "NEW " + board.encode());
            }
        });
        
        return board;
    }
    
    /**
     * Creates a whiteboard with a unique id and the given name
     */
    private synchronized ServerWhiteboard instantiateWhiteboard(String name) {
        synchronized(lastUsedId) {
            return new ServerWhiteboard(++lastUsedId, name);
        }
    }
}
