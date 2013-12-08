package client.networking;

import java.util.ArrayList;

import ui.client.WhiteboardMenuItem;

public interface MenuDelegate {

    public void onMenu(ArrayList<WhiteboardMenuItem> menus);

    public void onNew(ArrayList<WhiteboardMenuItem> menus);

    public void onBadID();
}