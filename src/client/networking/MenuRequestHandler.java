package client.networking;

import java.util.ArrayList;

import client.WhiteboardMenuItem;

public class MenuRequestHandler implements RequestHandler {

    private final MenuDelegate menuDelegate;

    public MenuRequestHandler(MenuDelegate delegate) {
        this.menuDelegate = delegate;
    }

    public void parseString(String input) {
        String[] parsed = input.split(" ");
        if (parsed[0].equals("MENU")) {
            ArrayList<WhiteboardMenuItem> menus = new ArrayList<WhiteboardMenuItem>();
            WhiteboardMenuItem menu = null;
            int i = 1;
            while (i < parsed.length) {
                int id = Integer.parseInt(parsed[i]);
                String name = parsed[i + 2];
                menu = new WhiteboardMenuItem(id, name);
                i = i + 3;
                menus.add(menu);
            }
            menuDelegate.onMenu(menus);

        } else if (parsed[0].equals("NEW")) {
            WhiteboardMenuItem menu = new WhiteboardMenuItem(
                    Integer.parseInt(parsed[1]), parsed[3]);
            ArrayList<WhiteboardMenuItem> menus = new ArrayList<WhiteboardMenuItem>();
            menus.add(menu);
            menuDelegate.onNew(menus);

        } else if (parsed[0].equals("BADID")) {
            menuDelegate.onBadID();
        }
    }
}
