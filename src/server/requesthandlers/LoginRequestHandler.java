package server.requesthandlers;

import server.messaging.*;
import server.ClientHandler;

public class LoginRequestHandler implements RequestHandler {
    private final static String NICK_OK = "NICKOK";
    private final static String SPECIFY_NICK = "SPECIFYNICK";
    private final static String NICK_IN_USE = "NICKINUSE";
    
    private final MessageBus messageBus;
    private final ClientHandler clientHandler;
    
    public LoginRequestHandler(MessageBus messageBus, ClientHandler clientHandler) {
        this.messageBus = messageBus;
        this.clientHandler = clientHandler;
    }
    
    public void onEnter() {
    }
    
    public void onLeave() {
    }
    
    @Override
    public void handle(String request) {
        String[] split = request.toLowerCase().split(" ");
        
        if (!isValid(split)) {
            clientHandler.log("Invalid request: " + request);
            return;
        }
        
        clientHandler.addMessage(NICK_OK);
        
        clientHandler.setNickname(split[1]);
        messageBus.subscribeClient(clientHandler);
        clientHandler.log("Set nickname to " + clientHandler.getNickname());
        
        clientHandler.setCurrentRequestHandler(new MenuRequestHandler(messageBus, clientHandler));
    }
    
    // returns whether the request is valid or not
    // and also sends a message to the client if necessary
    private boolean isValid(String[] request) {
        if (request.length == 0)
            return false;
        
        if (!request[0].equals("nick") || request.length != 2) {
            clientHandler.log("Didn't specify nickname");
            clientHandler.addMessage(SPECIFY_NICK);
            return false;
        }
        
        if (messageBus.hasClientNickname(request[1])) {
            clientHandler.log("Using the nickname already");
            clientHandler.addMessage(NICK_IN_USE);
            return false;
        }
        
        clientHandler.log("Valid nickname");
        return true;
    }
}
