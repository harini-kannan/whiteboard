package client.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import domain.Drawable;

/**
 * ClientSocket implements Runnable and handles both incoming and outgoing 
 * messages to the server. It has an instance of a RequestHandler that parses
 * handles messages from the server, and it also has a ConcurrentLinkedQueue to
 * store outgoing messages to the server.
 * 
 * This class is thread-safe because the only shared memory (the messages queue) is wrapped in a thread-safe ConcurrentLinkedQueue object.
 * This means that all reads and writes to the messages queue are atomic, which is why all the public methods to send messages (sendNickname, sendMake, etc.) are thread-safe.
 * @author hkannan
 * 
 */

/**
 * Manual testing strategy for public method run(): Tests that messages are
 * successfully sent, that messages are successfully received, and that the
 * thread waits in the case where the input and output buffers are empty.
 * 
 * @author hkannan
 * 
 */
public class ClientSocket implements Runnable {
    RequestHandler handler;
    private final Socket socket;
    private final ConcurrentLinkedQueue<String> messages;

    public ClientSocket(Socket s) {
        this.socket = s;
        this.messages = new ConcurrentLinkedQueue<>();
    }

    /**
     * Receives incoming messages from the server and sends messages from the
     * message queue to the server.
     */
    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            while (true) {
                if (messages.isEmpty() && !in.ready()) {
                    Thread.sleep(50);
                }
                String message = null;
                while ((message = messages.poll()) != null) {
                    System.out.println("<CLIENT> Sending: " + message);
                    out.println(message);

                    if (message == "BYE") {
                        System.out.println("<CLIENT> Exiting");
                        out.flush();
                        return;
                    }
                }
                String line = null;
                while (in.ready()) {
                    line = in.readLine();
                    System.out.println("<CLIENT> Received: " + line);
                    handler.parseString(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The GUI calls this method to also change the RequestHandler when a window
     * switches to the Login, Menu, or Drawing window.
     * 
     * @param h
     *            Instance of the RequestHandler that the GUI changes.
     */
    public void switchHandler(RequestHandler h) {
        this.handler = h;
    }

    /**
     * The following methods correspond to the Client to Server grammar detailed
     * below: Client to Server
     * 
     * Login window nickname := "NICK" NICKNAME NEWLINE
     * 
     * Menu window menu_action := [ make | join ] make := "MAKE" BOARD_NAME
     * NEWLINE join := "JOIN" BOARD_ID NEWLINE Drawing window on_draw :=
     * drawing_action leave := "LEAVE" NEWLINE Exit bye := "BYE" NEWLINE
     * 
     */

    /**
     * Sends message from the Login window. Corresponds to the following
     * grammar: nickname := "NICK" NICKNAME NEWLINE
     * 
     * @param nickname
     *            Nickname from user input.
     */
    public void sendNickname(String nickname) {
        messages.add("NICK " + nickname);
    }

    /**
     * Sends message from the Menu window. Corresponds to the following grammar:
     * make := "MAKE" BOARD_NAME
     * 
     * @param boardName
     *            Name of board from user input.
     */
    public void sendMake(String boardName) {
        messages.add("MAKE " + boardName);
    }

    /**
     * Sends message from the Menu window. Corresponds to the following grammar:
     * join := "JOIN" BOARD_ID NEWLINE
     * 
     * @param boardID
     */
    public void sendJoin(int boardID) {
        messages.add("JOIN " + boardID);
    }

    /**
     * Sends message from the Drawing window. Corresponds to the following
     * grammar: leave := "LEAVE" NEWLINE
     */
    public void sendLeave() {
        messages.add("LEAVE");
    }

    /**
     * Sends message from the Drawing window. Corresponds to the following
     * grammar: on_draw := drawing_action drawing_action := "DRAW" stroke
     * NEWLINE stroke := "STROKE" COLOR THICKNESS POINT THICKNESS := INT COLOR
     * := INT POINT := INT "," INT INT := [1-9][0-9]*
     * 
     * @param s
     *            Drawable object from the user.
     */
    public void sendDrawMessage(Drawable s) {
        messages.add(s.encode());
    }

    /**
     * Sends message from the Drawing window. Corresponds to the following
     * grammar: bye := "BYE" NEWLINE
     */
    public void sendBye() {
        messages.add("BYE");
    }
}
