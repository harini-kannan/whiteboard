package server;

import server.messaging.*;

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
    
    private synchronized ServerWhiteboard instantiateWhiteboard(String name) {
        synchronized(lastUsedId) {
            return new ServerWhiteboard(++lastUsedId, name);
        }
    }
}
