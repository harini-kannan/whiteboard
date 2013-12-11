package integrationtests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * @category no_didit
 */
public class MenuTests {
    
    @BeforeClass
	public static void setUp() {
		TestUtil.startServer();
	}
	
    @Test
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
    /*
    @Test
    public void joinBoardTest() throws IOException {
        Socket socket = TestUtil.connect();
        
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        
        out.println("NICK you-should-give-us-an-A");
        assertEquals("NICKOK", in.readLine());
        assertEquals("MENU", in.readLine());
        
        out.println("MAKE you-should-give-us-an-A");
        
        Socket otherSocket = TestUtil.connect();
        
        PrintWriter otherOut = new PrintWriter(otherSocket.getOutputStream(), true);
        BufferedReader otherIn = new BufferedReader(new InputStreamReader(otherSocket.getInputStream()));
        
        out.println("NICK hello-friend");
        assertEquals("NICKOK", in.readLine());
        
        assertEquals("MENU 1-you-should-give-us-an-A", in.readLine());
    }
    */

}