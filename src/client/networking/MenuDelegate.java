package client.networking;

import java.util.ArrayList;

import client.WhiteboardMenuItem;


/**
 * MenuDelegate is an interface whose callback methods will be implemented in the MenuGUI.
 * 
 */
public interface MenuDelegate {
    /**
     * Called by the MenuRequestHandler as a response to a request for 
     * the current list of boards on the server
     * 
     * @param menus An ArrayList of WhiteboardMenuItems with each server board
     */
    public void onMenuResponse(ArrayList<WhiteboardMenuItem> menus);
    
    /**
     * Called by the MenuRequestHandler when a new board is created on the
     * server.
     * 
     * @param menus The new WhiteboardMenuItem representing the new baord
     */
    public void onNewMenuItemRecieved(WhiteboardMenuItem menus);
    
    /**
     * Called by the MenuRequestHandler when a the requested board to join
     * cannot be resolved to a valid ID.
     * 
     */
    public void onInvalidBoardIDRequest();
}
