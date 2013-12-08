package client.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import domain.Drawable;

public class ClientSocket implements Runnable {

    RequestHandler handler;
    private final Socket socket;
    private final ConcurrentLinkedQueue<String> messages;

    public ClientSocket(Socket s) {
        this.socket = s;
        this.messages = new ConcurrentLinkedQueue<>();
    }

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
                    out.println(message);
                }
                String line = null;
                while (in.ready()) {
                    line = in.readLine();
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

    public void switchHandler(RequestHandler h) {
        this.handler = h;
    }

    public void sendNickname(String nickname) {
        messages.add("NICK " + nickname + "\n");
    }

    public void sendMake(String boardName) {
        messages.add("MAKE " + boardName + "\n");
    }

    public void sendJoin(int boardID) {
        messages.add("JOIN " + boardID + "\n");
    }

    public void sendLeave() {
        messages.add("LEAVE \n");
    }

    public void sendDrawMessage(Drawable s) {
        messages.add(s.encode());
    }

    public void sendBye() {
        messages.add("BYE\n");
    }
}
