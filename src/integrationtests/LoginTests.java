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
 * This is the full end-to-end test of the program, testing the login
 * functionality.
 * 
 * @category no_didit
 */
public class LoginTests {

    @BeforeClass
    public static void setUp() {
        TestUtil.startServer();
    }

    @Test
    // Test sending a command wihtout logging in
    public void serverSendsSpecifyNickWhenNoNickTest() throws IOException {
        Socket socket = TestUtil.connect();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        out.println("MENU");
        assertEquals("SPECIFYNICK", in.readLine());
    }

    @Test
    // Test logging in with a valid nickname
    public void serverSendsNickOkayWhenNickValidTest() throws IOException {
        Socket socket = TestUtil.connect();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        out.println("NICK you-should-give-us-an-A");
        assertEquals("NICKOK", in.readLine());
    }


    @Test
    // Test the same nickname is not supported.
    public void serverSendsNickInUseWhenNickIsTakenTest() throws IOException {
        Socket socket = TestUtil.connect();
        Socket otherSocket = TestUtil.connect();

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        PrintWriter otherOut = new PrintWriter(otherSocket.getOutputStream(),
                true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                otherSocket.getInputStream()));

        out.println("NICK you-should-give-us-an-A");
        otherOut.println("NICK you-should-give-us-an-A");

        assertEquals("NICKINUSE", in.readLine());
    }
}
