package server.messaging;

import server.ClientHandler;

/**
 * Wraps sending a String message to a client since that's a very
 * common action
 */
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
