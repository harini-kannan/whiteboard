package client.networking;

import domain.Drawable;

/**
 * DrawingDelegate is an delegate whose callback methods will be implemented in the WhiteboardGUI.
 * 
 */
public interface DrawingDelegate {
    
    /**
     * Called by the DrawingRequestHandler when a new Drawable object
     * is received from the server
     * 
     * @param d The Drawable object from the server
     */
    public void onNewDrawableRecieved(Drawable d);

    /**
     * Called by the DrawingRequestHandler when a new user signs
     * on to the current board.
     * 
     * @param username The username of the user that signed on
     */
    public void onUserSignOn(String username);

    /**
     * Called by the DrawingRequestHandler when a new user signs
     * off of the current board.
     * 
     * @param username The username of the user that signed off
     */
    public void onUserSignOff(String username);
}
