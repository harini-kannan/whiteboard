package client.networking;

import java.util.ArrayList;

import client.WhiteboardMenuItem;

/**
 * MenuRequestHandler parses and handles all incoming messages when in the Menu
 * window.
 * 
 * @author hkannan
 * 
 */

public class MenuRequestHandler implements RequestHandler {

    private final MenuDelegate menuDelegate;

    public MenuRequestHandler(MenuDelegate delegate) {
        this.menuDelegate = delegate;
    }

    /**
     * 
     * @param input
     *            String of the form "id - name"
     * @return WhiteboardMenuItem
     * 
     */
    private WhiteboardMenuItem parseWhiteboard(String input) {
        String[] splitBoard = input.split("-");

        int id = Integer.parseInt(splitBoard[0]);
        String name = splitBoard[1];

        return new WhiteboardMenuItem(id, name);
    }

    /**
     * Parses input according to the following grammar. Calls the relevant
     * delegate method.
     * 
     * menu_list := ÒMENUÓ BOARD* NEWLINE 
     * board_created := ÒNEWÓ BOARD NEWLINE
     * id_not_found := ÒBADIDÓ NEWLINE
     */
    public void parseString(String input) {
        String[] parsed = input.split(" ");
        if (parsed[0].equals("MENU")) {
            ArrayList<WhiteboardMenuItem> menus = new ArrayList<WhiteboardMenuItem>();

            for (int i = 1; i < parsed.length; i++)
                menus.add(parseWhiteboard(parsed[i]));

            menuDelegate.onMenuResponse(menus);

        } else if (parsed[0].equals("NEW")) {
            WhiteboardMenuItem menu = parseWhiteboard(parsed[1]);
            menuDelegate.onNewMenuItemRecieved(menu);

        } else if (parsed[0].equals("BADID")) {
            menuDelegate.onInvalidBoardIDRequest();
        }
    }
}
