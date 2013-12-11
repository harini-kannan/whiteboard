package client.networkingtests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import client.networking.DrawingDelegate;
import client.networking.DrawingRequestHandler;
import domain.Drawable;

/**
 * Testing strategy: parseString() is tested for the following three cases:
 * parsing a server message beginning with "DRAW" (for a stroke drawn by a
 * user), "JOIN" (when the user joins a whiteboard), and "LEAVE" (when the user
 * leaves a whiteboard).
 **/

public class DrawingRequestHandlerTests {

    @Test
    public void onNewDrawableReceivedTest() {
        DrawingDelegate delegate = new DrawingDelegate() {
            Drawable stroke = null;

            @Override
            public void onNewDrawableRecieved(Drawable d) {
                this.stroke = d;
            };

            @Override
            public void onUserSignOn(String username) {
                throw new RuntimeException("Should not enter onUserSignOn");
            };

            @Override
            public void onUserSignOff(String username) {
                throw new RuntimeException("Should not enter onUserSignOff");
            };

            @Override
            public String toString() {
                assertEquals("DRAW STROKE 0 1 1,1 20,20", stroke.encode());
                return "";
            };
        };
        String input = "DRAW STROKE 0 1 1,1 20,20";
        DrawingRequestHandler handler = new DrawingRequestHandler(delegate);
        handler.parseString(input);
        delegate.toString();
    }

    @Test
    public void onUserSignOnTest() {
        DrawingDelegate delegate = new DrawingDelegate() {
            String nickname = null;

            @Override
            public void onNewDrawableRecieved(Drawable d) {
                throw new RuntimeException(
                        "Should not enter onNewDrawableReceived");
            };

            @Override
            public void onUserSignOn(String username) {
                this.nickname = username;
            };

            @Override
            public void onUserSignOff(String username) {
                throw new RuntimeException("Should not enter onUserSignOff");
            };

            @Override
            public String toString() {
                assertEquals("bananas", nickname);
                return "";
            };
        };
        String input = "JOIN bananas \n";
        DrawingRequestHandler handler = new DrawingRequestHandler(delegate);
        handler.parseString(input);
        delegate.toString();
    }

    @Test
    public void onUserSignOffTest() {
        DrawingDelegate delegate = new DrawingDelegate() {
            String nickname = null;

            @Override
            public void onNewDrawableRecieved(Drawable d) {
                throw new RuntimeException(
                        "Should not enter onNewDrawableReceived");
            };

            @Override
            public void onUserSignOn(String username) {
                throw new RuntimeException("Should not enter onUserSignOff");
            };

            @Override
            public void onUserSignOff(String username) {
                this.nickname = username;
            };

            @Override
            public String toString() {
                assertEquals("bananas", nickname);
                return "";
            };
        };
        String input = "LEAVE bananas \n";
        DrawingRequestHandler handler = new DrawingRequestHandler(delegate);
        handler.parseString(input);
        delegate.toString();
    }

}
