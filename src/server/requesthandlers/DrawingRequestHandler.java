package server.requesthandlers;

import domain.Drawable;
import domain.DrawableParser;
import server.ClientHandler;
import server.ServerWhiteboard;
import server.messaging.*;

public class DrawingRequestHandler implements RequestHandler {
    private final MessageBus messageBus;
    private final ClientHandler clientHandler;
    private final Integer whiteboardId;
    
    private final DrawableParser drawableParser;
    
    public DrawingRequestHandler(MessageBus messageBus, ClientHandler clientHandler, Integer whiteboardId) {
        this.messageBus = messageBus;
        this.clientHandler = clientHandler;
        this.whiteboardId = whiteboardId;
        
        drawableParser = new DrawableParser();
    }
    
    @Override
    public void handle(String request) {
        String[] split = request.toLowerCase().split(" ");
        
        if (split.length == 0)
            return;
        
        // TODO(ddoucet): ew switch
        switch (split[0]) {
        case "draw":
            onDraw(split);
            break;
        case "leave":
            returnToMenu();
            break;
        default:
            // TODO(ddoucet): error?
            break;
        }
    }
    
    private void onDraw(String[] request) {
        if (request.length < 2)
            return;
        
        final Drawable drawable = drawableParser.parse(request);
        
        if (drawable == null)  // error
            return;  // TODO(ddoucet): be more helpful...

        messageBus.publishToWhiteboard(whiteboardId, new Action<ServerWhiteboard>() {
            @Override
            public void perform(ServerWhiteboard whiteboard) {
                whiteboard.addDrawable(drawable);
                whiteboard.publishToClients(messageBus, drawable.encode());
            }
        });
    }
    
    private void returnToMenu() {
        clientHandler.setCurrentRequestHandler(
            new MenuRequestHandler(messageBus, clientHandler));
    }
    
    public void onEnter() {
        messageBus.publishToWhiteboard(whiteboardId, new Action<ServerWhiteboard>() {
           @Override
            public void perform(ServerWhiteboard whiteboard) {
                whiteboard.addClient(clientHandler.getNickname());
                
                for (Drawable drawable : whiteboard.getDrawables())
                    messageBus.publishToClient(clientHandler.getNickname(), drawable.encode());
                
                String message = "JOIN " + clientHandler.getNickname();
                whiteboard.publishToClients(messageBus, message);
            }
        });
    }
    
    public void onLeave() {
        messageBus.publishToWhiteboard(whiteboardId, new Action<ServerWhiteboard>() {
            @Override
            public void perform(ServerWhiteboard whiteboard) {
                whiteboard.removeClient(clientHandler.getNickname());

                String message = "LEAVE " + clientHandler.getNickname();
                whiteboard.publishToClients(messageBus, message);
            }
        });
    }
}
