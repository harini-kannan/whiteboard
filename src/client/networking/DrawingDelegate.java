package client.networking;

import domain.Drawable;

public interface DrawingDelegate {
    public void addDrawableToWhiteBoard(Drawable d);
    public void signOnUser(String username);
    public void signOffUser(String username);
}
