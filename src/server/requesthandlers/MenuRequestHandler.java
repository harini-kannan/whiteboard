package server.requesthandlers;

import server.ClientHandler;
import server.ServerMenu;
import server.ServerWhiteboard;
import server.messaging.*;

// TODO(ddoucet): this should probably do something better than eat errors
// at least log them? maybe we ought to modify the grammar to return errors?
public class MenuRequestHandler implements RequestHandler {
    private final MessageBus messageBus;
    private final ClientHandler clientHandler;
    
    public MenuRequestHandler(MessageBus messageBus, ClientHandler clientHandler) {
        this.messageBus = messageBus;
        this.clientHandler = clientHandler;
    }
    
    @Override
    public void handle(String request) {
        String[] split = request.toLowerCase().split(" ");
        
        if (split.length == 0)
            return;
        
        // TODO(ddoucet): this could be better... but java isn't so great with
        // function pointers so ehhhh
        switch (split[0]) {
        case "make":
            requestMake(split);
            break;
        case "join":
            requestJoin(split);
            break;
        default:
            // TODO(ddoucet): should probably tell them they fucked up?
            return;
        }
    }
    
    // java doesn't have this, sigh...
    private Integer tryParse(String string) {
        try {
            return Integer.valueOf(string);
        }
        catch(NumberFormatException e) {
            return null;
        }
    }
    
    private void requestJoin(String[] request) {
        if (request.length != 2)
            return;  // TODO(ddoucet)
        
        Integer boardId = tryParse(request[1]);
        
        if (boardId == null ||  // not an integer
                !messageBus.hasWhiteboardId(boardId))
            return;
        
        joinBoard(boardId);
    }
    
    private void requestMake(String[] request) {
        if (request.length != 2)
            return;  // TODO(ddoucet)
        
        // TODO(ddoucet): validate board name? 
        
        ServerWhiteboard whiteboard = 
                messageBus
                    .getWhiteboardFactory()
                    .create(request[1]);
        
        joinBoard(whiteboard.getId());
    }
    
    private void joinBoard(Integer whiteboardId) {
        clientHandler.setCurrentRequestHandler(
            new DrawingRequestHandler(messageBus, clientHandler, whiteboardId));
    }
    
    public void onEnter() {
        requestMenuList();
        
        messageBus.publishToMenu(new Action<ServerMenu>() {
            @Override
            public void perform(ServerMenu t) {
                t.addClient(clientHandler.getNickname());
            }
        });
    }
    
    public void onLeave() {
        messageBus.publishToMenu(new Action<ServerMenu>() {
            @Override
            public void perform(ServerMenu t) {
                t.removeClient(clientHandler.getNickname());
            }
        });
    }
    
    public void requestMenuList() {
        messageBus.publishToMenu(new Action<ServerMenu>() {
            public void perform(ServerMenu t) {
                // menu_list := "MENU" BOARD* NEWLINE
                
                StringBuilder builder = new StringBuilder("MENU");
                
                for (ServerWhiteboard board : t.getWhiteboards())
                    builder.append(' ')
                        .append(board.encode());
                
                messageBus.publishToClient(
                    clientHandler.getNickname(), builder.toString());
            }
        });
    }
}
