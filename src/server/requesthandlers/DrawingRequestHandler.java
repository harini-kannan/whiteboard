package server.requesthandlers;

import domain.Drawable;
import domain.DrawableParser;
import server.ClientHandler;
import server.ServerWhiteboard;
import server.messaging.*;

/**
 * Handles requests when a client is in the menu state.
 * 
 * Transitions to MenuRequestHandler when the user leaves the board.
 * 
 * See RequestHandler
 */
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
        
        switch (split[0]) {
        case "draw":
            onDraw(split);
            break;
        case "leave":
            returnToMenu();
            break;
        default:
        	clientHandler.log("<ERROR> DrawingRequestHandler doesn't know how to handle " + request);
            break;
        }
    }
    
    private void onDraw(String[] request) {
        if (request.length < 2)
            return;
        
        final Drawable drawable = drawableParser.parse(request);
        
        if (drawable == null) {  // error
        	clientHandler.log("<ERROR> Unable to parse drawing request " + request);
            return;
        }

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
        	   // tell the client who is currently on the whiteboard
               for (String client : whiteboard.getClientUsernames())
                   messageBus.publishToClient(clientHandler.getNickname(), "JOIN " + client);

               // tell the client what drawing is currently on the whiteboard
               for (Drawable drawable : whiteboard.getDrawables())
                   messageBus.publishToClient(clientHandler.getNickname(), drawable.encode());
               
               // tell everyone that we've joined
               String message = "JOIN " + clientHandler.getNickname();
               whiteboard.publishToClients(messageBus, message);
               
               // add the client to the whiteboard
               whiteboard.addClient(clientHandler.getNickname());
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
