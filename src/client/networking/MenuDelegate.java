package client.networking;

import java.util.ArrayList;

import client.WhiteboardMenuItem;

public interface MenuDelegate {
    public void onMenuResponse(ArrayList<WhiteboardMenuItem> menus);
    public void onNewMenuItemRecieved(WhiteboardMenuItem menus);
    public void onInvalidBoardIDRequest();
}
