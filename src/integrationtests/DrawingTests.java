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
public class DrawingTests {
    
    @BeforeClass
	public static void setUp() {
		TestUtil.startServer();
	}
	

}
