package ui.client;

import domain.Drawable;

public interface WhiteboardClientDelegate {
    public void addDrawableToWhiteBoard(Drawable d);
    public void signOnUser(String username);
    public void signOffUser(String username);
}
