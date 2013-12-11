package integrationtests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import static org.junit.Assert.*;

import org.junit.*;
public class LoginTests {
	@Before
	public void setUp() {
		TestUtil.startServer();
	}
	
	@Test
	public void serverSendsSpecifyNickWhenNoNickTest() throws IOException {
		Socket socket = TestUtil.connect();
		
		 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(
                 socket.getInputStream()));
         
         out.println("MENU");
         assertEquals("SPECIFYNICK", in.readLine());
	}
}
