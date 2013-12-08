package client.networking;

public class MenuRequestHandler implements RequestHandler {

    private MenuDelegate menuDelegate;

    public void parseString(String input) {
        String[] parsed = input.split(" ");
        if (parsed[0].equals("MENU")) {
            menuDelegate.onMenu();

        } else if (parsed[0].equals("NEW")) {
            menuDelegate.onNew();

        } else if (parsed[0].equals("BADID")) {
            menuDelegate.onBadID();
        }
    }
}
