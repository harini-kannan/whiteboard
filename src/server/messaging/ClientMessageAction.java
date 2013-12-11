package server.messaging;

import server.ClientHandler;

// wraps sending the client a message since this will be used a lot
public class ClientMessageAction implements Action<ClientHandler> {
    private final String message;
    
    public ClientMessageAction(String message) {
        this.message = message;
    }
    
    @Override
    public void perform(ClientHandler t) {
        t.addMessage(message);
    }
}
