package client.networking;

import ui.client.WhiteboardClientDelegate;
import domain.Drawable;
import domain.DrawableParser;

public class DrawingRequestHandler implements RequestHandler {

    private WhiteboardClientDelegate drawingDelegate;

    @Override
    public void parseString(String input) {
        String[] parsed = input.split(" ");
        if (parsed[0].equals("LEAVE")) {
            drawingDelegate.signOffUser(parsed[1]);
        } else if (parsed[0].equals("JOIN")) {
            drawingDelegate.signOnUser(parsed[1]);
        } else if (parsed[0].equals("DRAW")) {
            DrawableParser parser = new DrawableParser();
            Drawable parserInput = parser.parse(parsed);
            drawingDelegate.addDrawableToWhiteBoard(parserInput);
        } else if (parsed[0].equals("MENU")) {
            // TODO(kulpreet): Add in a "on close" method to the whiteboard that
            // changes the handler to a MenuRequestHandler.
        }

    }
}
