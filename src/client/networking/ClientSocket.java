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
                    System.out.println("Sending " + message);
                    out.println(message);
                }
                String line = null;
                while (in.ready()) {
                    line = in.readLine();
                    System.out.println("Got a response!: " + line);
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
     * Login window nickname := ÒNICKÓ NICKNAME NEWLINE
     * 
     * Menu window menu_action := [ make | join ] make := ÒMAKEÓ BOARD_NAME
     * NEWLINE join := ÒJOINÓ BOARD_ID NEWLINE Drawing window on_draw :=
     * drawing_action leave := ÒLEAVEÓ NEWLINE Exit bye := ÒBYEÓ NEWLINE
     * 
     */

    /**
     * Sends message from the Login window. Corresponds to the following
     * grammar: nickname := ÒNICKÓ NICKNAME NEWLINE
     * 
     * @param nickname
     *            Nickname from user input.
     */
    public void sendNickname(String nickname) {
        messages.add("NICK " + nickname);
    }

    /**
     * Sends message from the Menu window. Corresponds to the following grammar:
     * make := ÒMAKEÓ BOARD_NAME
     * 
     * @param boardName
     *            Name of board from user input.
     */
    public void sendMake(String boardName) {
        messages.add("MAKE " + boardName);
    }

    /**
     * Sends message from the Menu window. Corresponds to the following grammar:
     * join := ÒJOINÓ BOARD_ID NEWLINE
     * 
     * @param boardID
     */
    public void sendJoin(int boardID) {
        messages.add("JOIN " + boardID);
    }

    /**
     * Sends message from the Drawing window. Corresponds to the following
     * grammar: leave := ÒLEAVEÓ NEWLINE
     */
    public void sendLeave() {
        messages.add("LEAVE");
    }

    /**
     * Sends message from the Drawing window. Corresponds to the following
     * grammar: on_draw := drawing_action drawing_action := ÒDRAWÓ stroke
     * NEWLINE stroke := ÒSTROKEÓ COLOR THICKNESS POINT THICKNESS := INT COLOR
     * := INT POINT := INT Ò,Ó INT INT := [1-9][0-9]*
     * 
     * @param s
     *            Drawable object from the user.
     */
    public void sendDrawMessage(Drawable s) {
        messages.add(s.encode());
    }

    /**
     * Sends message from the Drawing window. Corresponds to the following
     * grammar: bye := ÒBYEÓ NEWLINE
     */
    public void sendBye() {
        messages.add("BYE");
    }
}
