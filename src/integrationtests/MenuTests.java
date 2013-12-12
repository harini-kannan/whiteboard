package integrationtests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * This is the full end-to-end test of the program, testing the menu
 * functionality.
 * 
 * @category no_didit
 */
public class MenuTests {
    
    @BeforeClass
	public static void setUp() {
		TestUtil.startServer();
	}
	
    @Test
    // Tests the creation of a new board on the server
    public void newBoardCreationTest() throws IOException {
        Socket socket = TestUtil.connect();
        
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        
        out.println("NICK you-should-give-us-an-a");
        assertEquals("NICKOK", in.readLine());
        assertEquals("MENU", in.readLine());
        
        out.println("MAKE you-should-give-us-an-a");
        
        Socket otherSocket = TestUtil.connect();
        
        PrintWriter otherOut = new PrintWriter(otherSocket.getOutputStream(), true);
        BufferedReader otherIn = new BufferedReader(new InputStreamReader(otherSocket.getInputStream()));
        
        otherOut.println("NICK hello-friend");
        assertEquals("NICKOK", otherIn.readLine());
        
        String response = otherIn.readLine();
        if (response.equals("MENU")) {
            assertEquals("NEW 1-you-should-give-us-an-a", otherIn.readLine());
        }
        else {
            assertEquals("MENU 1-you-should-give-us-an-a", response);
        }
        
    }

    @Test
    // Tests joining a valid board on the server
    public void joinBoardTest() throws IOException {
        Socket socket = TestUtil.connect();
        
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        
        out.println("NICK hello2");
        assertEquals("NICKOK", in.readLine());
        if (in.readLine().equals("MENU 1-you-should-give-us-an-a")) {
            out.println("JOIN 1");
            assertEquals("JOIN you-should-give-us-an-a", in.readLine());
        }
        else if (in.readLine().equals("NEW 1-you-should-give-us-an-a")) {
            out.println("JOIN 1");
            assertEquals("JOIN you-should-give-us-an-a", in.readLine());
        }
    }
    
    @Test
    // Tests joining an invalid board on the server
    public void joinBadBoardTest() throws IOException {
        Socket socket = TestUtil.connect();
        
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        
        out.println("NICK hello3");
        assertEquals("NICKOK", in.readLine());
        if (in.readLine().equals("MENU 1-you-should-give-us-an-a")) {
            out.println("JOIN 5");
            assertEquals("BADID", in.readLine());
        }
        else if (in.readLine().equals("NEW 1-you-should-give-us-an-a")) {
            out.println("JOIN 5");
            assertEquals("BADID", in.readLine());
        }
    }


}