package client.networking;

import java.util.ArrayList;
import org.junit.Test;
import client.WhiteboardMenuItem;
import static org.junit.Assert.*;

public class MenuRequestHandlerTest {

    /** Expected output: in onBadID method **/
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
    public void parseStringonNewTest() {
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
}
