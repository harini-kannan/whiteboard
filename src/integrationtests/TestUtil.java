package integrationtests;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import server.Server;

public class TestUtil {
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
