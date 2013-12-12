package integrationtests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @category no_didit
 */
/**
 * This is the full end-to-end test of the program, testing the drawing
 * capability of the whiteboard.
 * 
 * @author hkannan
 * 
 */
public class DrawingTests {

    @BeforeClass
    public static void setUp() {
        TestUtil.startServer();
    }

    /**
     * This is a full end-to-end test.
     * 
     * 1) Ensures that the server runs. 2) Ensure multiple clients can connect.
     * 3) Ensures that joining/leaving a board updates connected users for both.
     * 4) Ensures that drawing on the board updates for the other. 5) Ensures
     * that joining the board after drawing has happened will show correct
     * picture. 6) Ensure that everyone disconnecting from the server and then
     * rejoining still has the same whiteboard. 7) Ensures that strokes are
     * overlayed on top of each other in chronological order.
     * 
     * @throws IOException
     */
    @Test
    public void drawStrokeTest() throws IOException {
        Socket socket = TestUtil.connect();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        // Creates user1.
        out.println("NICK user1");
        assertEquals("NICKOK", in.readLine());
        assertEquals("MENU", in.readLine());

        // User1 creates whiteboard.
        out.println("MAKE whiteboard");

        Socket otherSocket = TestUtil.connect();

        PrintWriter otherOut = new PrintWriter(otherSocket.getOutputStream(),
                true);
        BufferedReader otherIn = new BufferedReader(new InputStreamReader(
                otherSocket.getInputStream()));

        // Creates user2.
        otherOut.println("NICK user2");
        assertEquals("NICKOK", otherIn.readLine());

        String response = otherIn.readLine();
        if (response.equals("MENU")) {
            assertEquals("NEW 1-whiteboard", otherIn.readLine());
        } else {
            assertEquals("MENU 1-whiteboard", response);
        }

        // User 2 joins whiteboard. Both user1 and user2 should know that user2
        // has joined.
        otherOut.println("JOIN 1");
        assertEquals("JOIN user2", in.readLine());
        assertEquals("JOIN user1", otherIn.readLine());

        // User 1 draws a stroke. The whiteboard should update for both user1
        // and user2.
        out.println("DRAW STROKE 0 5 1,1 2,2");
        assertEquals("DRAW STROKE 0 5 1,1 2,2", in.readLine());
        assertEquals("DRAW STROKE 0 5 1,1 2,2", otherIn.readLine());

        // User 2 draws a stroke. The whiteboard should update for both user1
        // and user2.
        otherOut.println("DRAW STROKE 0 10 3,3 10,10");
        assertEquals("DRAW STROKE 0 10 3,3 10,10", in.readLine());
        assertEquals("DRAW STROKE 0 10 3,3 10,10", otherIn.readLine());

        // User 2 leaves whiteboard.
        otherOut.println("LEAVE 1");
        assertEquals("LEAVE user2", in.readLine());
        assertEquals("MENU 1-whiteboard", otherIn.readLine());

        // User 2 joins whiteboard again. User2 should receive all the strokes
        // saved on the whiteboard, in the order that the strokes were created.
        otherOut.println("JOIN 1");
        assertEquals("JOIN user2", in.readLine());
        assertEquals("JOIN user1", otherIn.readLine());
        assertEquals("DRAW STROKE 0 5 1,1 2,2", otherIn.readLine());
        assertEquals("DRAW STROKE 0 10 3,3 10,10", otherIn.readLine());

    }
}
