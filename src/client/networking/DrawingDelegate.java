package client.networking;

import domain.Drawable;

/**
 * DrawingDelegate is an interface whose methods will be implemented in the GUI.
 * 
 * @author hkannan
 * 
 */
public interface DrawingDelegate {
    public void onNewDrawableRecieved(Drawable d);

    public void onUserSignOn(String username);

    public void onUserSignOff(String username);
}
