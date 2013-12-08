package client;

import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

public class Main {

    /*
     * Main program. Make a window containing a WhiteboardPanel.
     */
    public static void main(String[] args) {
        // set up the UI (on the event-handling thread)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Socket s;
                try {
                    s = new Socket("127.0.0.1", 4444);
                    LoginGUI loginGUI = new LoginGUI(s);
                    loginGUI.requestLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
