package client.networking;

/**
 * All client-side handlers implement the RequestHandler interface.
 * 
 * @author hkannan
 * 
 */
public interface RequestHandler {

    /**
     * Parses server message and calls the correct delegate action.
     * 
     * @param input
     *            String message from the server.
     */
    void parseString(String input);

}
