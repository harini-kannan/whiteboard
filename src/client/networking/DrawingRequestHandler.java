package client.networking;

import domain.Drawable;
import domain.DrawableParser;

/**
 * DrawingRequestHandler parses and handles all incoming messages when in the
 * Drawing window.
 * 
 * @author hkannan
 * 
 */
public class DrawingRequestHandler implements RequestHandler {

    private final DrawingDelegate drawingDelegate;

    public DrawingRequestHandler(DrawingDelegate drawingDelegate) {
        this.drawingDelegate = drawingDelegate;
    }

    /**
     * Parses input according to the following grammar. Then calls relevant
     * delegate method. on_friend_join := ÒLEAVEÓ NICKNAME NEWLINE
     * on_friend_leave := ÒJOINÓ NICKNAME NEWLINE on_friend_draw :=
     * drawing_action drawing_action := ÒDRAWÓ stroke NEWLINE
     */
    @Override
    public void parseString(String input) {
        String[] parsed = input.toLowerCase().split(" ");
        if (parsed[0].equals("leave")) {
            drawingDelegate.onUserSignOff(parsed[1]);
        } else if (parsed[0].equals("join")) {
            drawingDelegate.onUserSignOn(parsed[1]);
        } else if (parsed[0].equals("draw")) {
            DrawableParser parser = new DrawableParser();
            Drawable parserInput = parser.parse(parsed);

            if (parserInput == null) {
                System.out.println("Unable to parse drawable " + input);
                return;
            }
            drawingDelegate.onNewDrawableRecieved(parserInput);
        } else {
            // TODO(ddoucet): error message -- at least log it
        }
    }
}
