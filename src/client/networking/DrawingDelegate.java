package client.networking;

import domain.Drawable;

public interface DrawingDelegate {
    public void onNewDrawableRecieved(Drawable d);
    public void onUserSignOn(String username);
    public void onUserSignOff(String username);
}
