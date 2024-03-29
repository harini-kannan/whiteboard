package server.clienthandlertests;

import java.util.*;

import server.ClientHandler;
import server.requesthandlers.RequestHandler;

/**
 * Extends ClientHandler to make the request handler and messages
 * publicly available for testing.
 */
public class TestableClientHandler extends ClientHandler {
    public TestableClientHandler() {
        super(0, null);  // not a client, no logger
    }

    public RequestHandler getCurrentRequestHandler() {
        return currentHandler;
    }
    
    public List<String> getAllMessages() {
        List<String> ret = new ArrayList<>();
        String message = null;
        
        while ((message = messages.poll()) != null)
            ret.add(message);
        
        return ret;
    }
}
