package client.networkingtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import client.WhiteboardMenuItem;
import client.networking.MenuDelegate;
import client.networking.MenuRequestHandler;

public class MenuRequestHandlerTest {

    /**
     * Testing strategy: parseString() is tested for the following three cases:
     * parsing a server message beginning with "BADID" (for a bad ID), "NEW"
     * (when the user creates a new whiteboard), and "MENU" (when the user pulls
     * up the menu of current whiteboards).
     **/
    @Test
    public void parseStringBadIDTest() {
        MenuDelegate delegate = new MenuDelegate() {
            public boolean enteredOnBadId = false;

            @Override
            public void onMenuResponse(ArrayList<WhiteboardMenuItem> menus) {
                throw new RuntimeException("Should not enter onMenu");
            }

            @Override
            public void onNewMenuItemRecieved(WhiteboardMenuItem menu) {
                throw new RuntimeException("Should not enter onNew");
            };

            @Override
            public void onInvalidBoardIDRequest() {
                enteredOnBadId = true;
            };

            // HACK to make sure things get called
            @Override
            public String toString() {
                assertTrue(enteredOnBadId);
                return "";
            }
        };

        MenuRequestHandler handler = new MenuRequestHandler(delegate);

        String input = "BADID";
        handler.parseString(input);

        delegate.toString();
    }

    /** Expected output: in onNew method 24 bananas **/
    @Test
    public void parseStringonNewMenuItemReceivedTest() {
        MenuDelegate delegate = new MenuDelegate() {
            private WhiteboardMenuItem menuItem;

            @Override
            public void onMenuResponse(ArrayList<WhiteboardMenuItem> menus) {
                throw new RuntimeException("Should not enter onMenu");
            }

            @Override
            public void onNewMenuItemRecieved(WhiteboardMenuItem menu) {
                menuItem = menu;
            };

            @Override
            public void onInvalidBoardIDRequest() {
                throw new RuntimeException("Should not enter onBadId");
            };

            // HACK: see above
            @Override
            public String toString() {
                assertEquals(24, menuItem.getID());
                assertEquals("bananas", menuItem.getName());
                return "";
            }
        };

        MenuRequestHandler handler = new MenuRequestHandler(delegate);

        String input = "NEW 24-bananas";
        handler.parseString(input);

        delegate.toString();
    }

    /** Expected output: in onNew method 24 bananas **/
    @Test
    public void parseStringonMenuResponseTest() {
        MenuDelegate delegate = new MenuDelegate() {
            private ArrayList<WhiteboardMenuItem> menuItems;

            @Override
            public void onMenuResponse(ArrayList<WhiteboardMenuItem> menus) {
                menuItems = menus;
            }

            @Override
            public void onNewMenuItemRecieved(WhiteboardMenuItem menu) {
                throw new RuntimeException(
                        "Should not enter onNewMenuItemReceived");
            };

            @Override
            public void onInvalidBoardIDRequest() {
                throw new RuntimeException(
                        "Should not enter onInvalidBoardIDRequest");
            };

            // HACK: see above
            @Override
            public String toString() {
                assertEquals(24, menuItems.get(0).getID());
                assertEquals("bananas", menuItems.get(0).getName());
                assertEquals(10, menuItems.get(1).getID());
                assertEquals("milk", menuItems.get(1).getName());
                assertEquals(15, menuItems.get(2).getID());
                assertEquals("cookies", menuItems.get(2).getName());
                assertEquals(20, menuItems.get(3).getID());
                assertEquals("santa", menuItems.get(3).getName());
                return "";
            }
        };

        MenuRequestHandler handler = new MenuRequestHandler(delegate);

        String input = "MENU 24-bananas 10-milk 15-cookies 20-santa";
        handler.parseString(input);

        delegate.toString();
    }
}
