package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import javax.swing.SwingUtilities;
import client.networking.ClientSocket;

/**
 * Main client program for Whiteboard GUI client interface.
 * 
 * Usage: Client.java [--debug] [--port PORT] [--host HOSTNAME]
 * 
 * Thread Safety Argument: This class spins the main thread with the ClientSocket.
 * It invokes the GUI on the event handling thread to ensure thread safety. Additionally, 
 * the only field socket is final.
 */
public class Client {
	private final Socket socket;
	
	public Client(boolean debug, String hostname, int port) throws UnknownHostException, IOException  {
		this.socket = debug ? new Socket() : new Socket(hostname, port);
	}
	
	/**
	 * Runs the set up client
	 */
	public void run() {
		final ClientSocket clientSocket = new ClientSocket(socket);
        
        Thread socketThread = new Thread(clientSocket);
        socketThread.start();
        
        // Set up the UI (on the event-handling thread)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		LoginGUI loginGUI = new LoginGUI(clientSocket);
                loginGUI.requestLogin();
            }
        });
	}
	
    public static void main(String[] args) throws UnknownHostException, IOException {
        int port = 4444; // default port
        String hostname = "127.0.0.1"; //default hostname
        boolean debug = false;

        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while (!arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--debug")) {
                        debug = true;
                    } else if (flag.equals("--no-debug")) {
                        debug = false;
                    } else if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());
                        if (port < 0 || port > 65535) {
                            throw new IllegalArgumentException("Port " + port + " out of range");
                        }
                    } else if (flag.equals("--host")) {
                        hostname = arguments.remove();
                    } else {
                        throw new IllegalArgumentException("Unknown option: \"" + flag + "\"");
                    }
                } catch (NoSuchElementException nsee) {
                    throw new IllegalArgumentException("Missing argument for " + flag);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("Usage: Main.java [--debug] [--port PORT] [--host HOSTNAME]");
            return;
        }
        
        new Client(debug, hostname, port).run();
    }
}
