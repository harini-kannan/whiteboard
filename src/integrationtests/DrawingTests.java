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
public class DrawingTests {

    @BeforeClass
    public static void setUp() {
        TestUtil.startServer();
    }

    /**
     * 1) Ensure multiple clients can connect. 2) Ensures that joining/leaving a
     * board updates connected users for both. 3) Ensures that drawing on the
     * board updates for the other. 4) Ensures that joining the board after
     * drawing has happened will show correct picture.
     * 
     * @throws IOException
     */
    @Test
    public void drawStrokeTest() throws IOException {
        Socket socket = TestUtil.connect();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        out.println("NICK user1");
        assertEquals("NICKOK", in.readLine());
        assertEquals("MENU", in.readLine());

        out.println("MAKE whiteboard");

        Socket otherSocket = TestUtil.connect();

        PrintWriter otherOut = new PrintWriter(otherSocket.getOutputStream(),
                true);
        BufferedReader otherIn = new BufferedReader(new InputStreamReader(
                otherSocket.getInputStream()));

        otherOut.println("NICK user2");
        assertEquals("NICKOK", otherIn.readLine());

        String response = otherIn.readLine();
        if (response.equals("MENU")) {
            assertEquals("NEW 1-whiteboard", otherIn.readLine());
        } else {
            assertEquals("MENU 1-whiteboard", response);
        }

        // User 2 joins whiteboard.
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

        // System.out.println(otherIn.readLine());
    }
}
