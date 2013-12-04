package ui.client;

import java.util.HashMap;

import domain.Drawable;

public class WhiteboardClient {
    private WhiteboardClientDelegate clientDelegate;
    
    public WhiteboardClient() {
        
    }
    
    public void setDelegate(WhiteboardClientDelegate clientDelegate) {
        this.clientDelegate = clientDelegate;
    }
    
    public boolean loginUser(String userName) {
        return false; //Return false if the username is already in use
    }
    
    public HashMap<Integer,String> getMenu() {
        return null;
    }
    
    public int createBoard(String boardName) {
        return 0; //Returns the boardID of the created board
    }
    
    public void joinBoard(int boardID) {
        
    }
    
    public void addDrawableToServerBoard(Drawable d) {
        
    }
    
    public void leaveBoard() {
        
    }
    
    
}
