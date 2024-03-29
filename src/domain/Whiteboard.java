package domain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Whiteboard is a datatype representing a drawing board. It has a unique ID,
 * name, a list of users connected to the board, and a list of Drawable
 * components on the board.
 * 
 * Thread-safety argument: All fields of this class are private and final. The
 * only mutator methods are signInUser, signOffUser, and addDrawable, which are
 * called in a thread-safe way as explained below.
 * 
 * Two parts of the code use this class:
 * 
 * 1) ServerWhiteboard -- private state, thus no threadsafety issues 2)
 * WhiteboardGUI -- also private state, thus no threadsafety issues
 * 
 * The client socket communicates with the whiteboard by calling the signInUser,
 * signOFfUser, and addDrawable methods on the WhiteboardGUI which use
 * swing.invokeLater() to ensure it's being accessed on only one thread (the GUI
 * thread)
 */
public class Whiteboard {
    private final int id;
    private final String name;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final List<String> usernames;
    private final List<Drawable> components;

    public Whiteboard(int id, String name) {
        this.id = id;
        this.name = name;
        this.usernames = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public Whiteboard(String name) {
        this.id = 0;
        this.name = name;
        this.usernames = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    /**
     * The Whiteboard draws itself and each of its components in turn.
     * 
     * @param graphics
     *            The Graphics 2D object that the whiteboard is being drawn
     *            onto.
     * 
     */
    public void drawTo(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        for (Drawable d : this.components) {
            d.drawTo(graphics);
        }
    }

    /**
     * Add user to the list of users being maintained by the Whiteboard
     * 
     * @param username
     *            Name of user who wants to be added to the current whiteboard.
     */
    public void signInUser(String username) {
        usernames.add(username);
    }

    /**
     * Remove user from the list of users being maintained by the Whiteboard
     * 
     * @param username
     *            Name of user who will be removed from the current whiteboard.
     */
    public void signOffUser(String username) {
        usernames.remove(username);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getUsers() {
        return this.usernames;
    }

    public List<Drawable> getDrawables() {
        return new ArrayList<>(components);
    }

    public void addDrawable(Drawable d) {
        components.add(d);
    }

}
