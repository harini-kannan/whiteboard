package ui.client;

import java.util.ArrayList;

import domain.Drawable;
import domain.Whiteboard;

public class WhiteboardClient {
    private WhiteboardClientDelegate clientDelegate;
    
    public WhiteboardClient() {
        
    }
    
    public void setDelegate(WhiteboardClientDelegate clientDelegate) {
        this.clientDelegate = clientDelegate;
    }
    
    public boolean loginUser(String userName) {
        return true; //Return false if the username is already in use
    }
    
    public ArrayList<WhiteboardMenuItem> getMenu() {
        ArrayList<WhiteboardMenuItem> testReturn = new ArrayList<WhiteboardMenuItem>();
        testReturn.add(new WhiteboardMenuItem(1,"Hello"));
        return testReturn;
    }
    
    public int createBoard(String boardName) {
        return 0; //Returns the boardID of the created board
    }
    
    public Whiteboard joinBoard(int boardID) {
        return new Whiteboard(boardID, "Hello");
    }
    
    public void addDrawableToServerBoard(Drawable d) {
        
    }
    
    public void leaveBoard() {
        
    }
    
    //TODO: Call clientDelegate.addDrawableToWhiteBoard(Drawable d);
    //TODO: Call clientDelegate.signOnUser(String username);
    //TODO: Call clientDelegate.signOffUser(String username);
    
    
}
