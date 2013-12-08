package server.clienthandlertests;

import java.util.*;

import server.ClientHandler;
import server.requesthandlers.RequestHandler;

public class TestableClientHandler extends ClientHandler {
    public TestableClientHandler() {
        super();
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
