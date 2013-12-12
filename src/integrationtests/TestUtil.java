package integrationtests;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import server.Server;


/**
 * Utility for end-to-end test suite. Can be used to start a server
 * and connect Sockets to it.
 *
 */
public class TestUtil {
    
    /**
     * Starts the server. You can call this in the "@BeforeClass" of
     * a JUnit test suite to run tests against.
     */
	public static void startServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Server.main(new String[0]);
				} catch (IOException e) {
					System.out.println("SERVER ERROR");
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * Create a socket connected to the server above for testing
	 * purposes.
	 * 
	 * @return A socket connected to the server started above
	 * @throws IOException
	 */
	public static Socket connect() throws IOException {
		Socket ret = null;
	    final int MAX_ATTEMPTS = 50;
	    int attempts = 0;
	    do {
	        try {
	            ret = new Socket("127.0.0.1", 4444);
	        } catch (ConnectException ce) {
	            try {
	                if (++attempts > MAX_ATTEMPTS)
	                    throw new IOException("Exceeded max connection attempts", ce);
	                Thread.sleep(300);
	            } catch (InterruptedException ie) {
	                throw new IOException("Unexpected InterruptedException", ie);
	            }
	        }
	    } while (ret == null);
	      
	    ret.setSoTimeout(3000);
	    return ret;
	}
}
