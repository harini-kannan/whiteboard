package client.networking;

import java.util.ArrayList;

import org.junit.Test;

import client.WhiteboardMenuItem;

public class MenuRequestHandlerTest {

    /** Expected output: in onBadID method **/
    @Test
    public void parseStringBadIDTest() {
        MenuDelegate delegate;

        MenuRequestHandler handler = new MenuRequestHandler(new MenuDelegate() {
            public void onMenu(ArrayList<WhiteboardMenuItem> menus) {
                System.out.println("in onMenu method");
            }

            public void onNew(WhiteboardMenuItem menu) {
                System.out.println("in onNew method");
            };

            public void onBadID() {
                System.out.println("in onBadID method");
            };
        });
        String input = "BADID";
        handler.parseString(input);
    }

    /** Expected output: in onNew method 24 bananas **/
    @Test
    public void parseStringonNewTest() {
        MenuDelegate delegate;

        MenuRequestHandler handler = new MenuRequestHandler(new MenuDelegate() {
            public void onMenu(ArrayList<WhiteboardMenuItem> menus) {
                System.out.println("in onMenu method");
                for (WhiteboardMenuItem menu : menus) {
                    System.out.println("moo");
                }
            }

            public void onNew(WhiteboardMenuItem menu) {
                System.out.println("in onNew method");
                System.out.println(menu.toString());
            };

            public void onBadID() {
                System.out.println("in onBadID method");
            };
        });
        String input = "NEW 24 - bananas";
        handler.parseString(input);
    }

}
