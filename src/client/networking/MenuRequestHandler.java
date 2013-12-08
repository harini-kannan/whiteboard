package client.networking;

import java.util.ArrayList;

import client.WhiteboardMenuItem;

public class MenuRequestHandler implements RequestHandler {

    private final MenuDelegate menuDelegate;

    public MenuRequestHandler(MenuDelegate delegate) {
        this.menuDelegate = delegate;
    }
    
    private WhiteboardMenuItem parseWhiteboard(String input) {
        String[] splitBoard = input.split("-");
        
        int id = Integer.parseInt(splitBoard[0]);
        String name = splitBoard[1];
        
        return new WhiteboardMenuItem(id, name);
    }

    public void parseString(String input) {
        String[] parsed = input.split(" ");
        if (parsed[0].equals("MENU")) {
            ArrayList<WhiteboardMenuItem> menus = new ArrayList<WhiteboardMenuItem>();
            
            for (int i = 1; i < parsed.length; i++)
                menus.add(parseWhiteboard(parsed[i]));
            
            menuDelegate.onMenu(menus);
        } else if (parsed[0].equals("NEW")) {
            WhiteboardMenuItem menu = parseWhiteboard(parsed[1]);
            menuDelegate.onNew(menu);

        } else if (parsed[0].equals("BADID")) {
            menuDelegate.onBadID();
        }
    }
}
