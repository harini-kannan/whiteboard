package client.networking;

import domain.Drawable;
import domain.DrawableParser;

public class DrawingRequestHandler implements RequestHandler {

    private final DrawingDelegate drawingDelegate;
    
    public DrawingRequestHandler(DrawingDelegate drawingDelegate) {
        this.drawingDelegate = drawingDelegate;
    }

    @Override
    public void parseString(String input) {
        String[] parsed = input.toLowerCase().split(" ");
        if (parsed[0].equals("leave")) {
            drawingDelegate.signOffUser(parsed[1]);
        } else if (parsed[0].equals("join")) {
            drawingDelegate.signOnUser(parsed[1]);
        } else if (parsed[0].equals("draw")) {
            DrawableParser parser = new DrawableParser();
            Drawable parserInput = parser.parse(parsed);
            
            if (parserInput == null) {
                System.out.println("Unable to parse drawable " + input);
                return;
            }
            
            drawingDelegate.addDrawableToWhiteBoard(parserInput);
        }
        else {
            // TODO(ddoucet): error message -- at least log it
        }
    }
}
