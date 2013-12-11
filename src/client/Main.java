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

public class Main {

    /*
     * Main client program for Whiteboard GUI.
     * 
     * Usage: Main.java [--debug] [--port PORT] [--host HOSTNAME]
     */
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

        run(debug ? new Socket() : new Socket(hostname, port));
    }

    private static void run(final Socket s) {
        final ClientSocket clientSocket = new ClientSocket(s);
        
        Thread socketThread = new Thread(clientSocket);
        socketThread.start();
        
        // set up the UI (on the event-handling thread)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		LoginGUI loginGUI = new LoginGUI(clientSocket);
                loginGUI.requestLogin();
            }
        });
    }
    
    
}
