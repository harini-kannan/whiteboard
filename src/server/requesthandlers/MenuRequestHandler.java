package server.requesthandlers;

import server.ClientHandler;
import server.ServerMenu;
import server.ServerWhiteboard;
import server.messaging.*;

public class MenuRequestHandler implements RequestHandler {
	private static final String BAD_ID = "BADID";
	
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
        
        switch (split[0]) {
        case "make":
            requestMake(split);
            break;
        case "join":
            requestJoin(split);
            break;
        default:
            clientHandler.log("<ERROR> MenuRequestHandler doesn't know how to handle " + request);
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
        if (request.length != 2) {
            clientHandler.log("<ERROR> Invalid request length for join");
            return;
        }
        
        Integer boardId = tryParse(request[1]);
        
        if (boardId == null ||  // not an integer
                !messageBus.hasWhiteboardId(boardId)) {
            clientHandler.addMessage(BAD_ID);
            return;
        }
        
        joinBoard(boardId);
    }
    
    private void requestMake(String[] request) {
        if (request.length != 2) {
            clientHandler.log("<ERROR> Invalid request length for make board (" + request.length + ")");
            return;
        }
        
        ServerWhiteboard whiteboard = 
            messageBus
                .getWhiteboardFactory()
                .create(request[1]);
        
        joinBoard(whiteboard.getId());
    }
    
    private void joinBoard(Integer whiteboardId) {
        clientHandler.log("<INFO> Joining board " + whiteboardId.toString());
        
        clientHandler.setCurrentRequestHandler(
            new DrawingRequestHandler(messageBus, clientHandler, whiteboardId));
    }
    
    public void onEnter() {
        messageBus.publishToMenu(new Action<ServerMenu>() {
            @Override
            public void perform(ServerMenu t) {
                t.addClient(clientHandler.getNickname());
            }
        });

        requestMenuList();
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
