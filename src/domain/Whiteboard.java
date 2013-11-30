package domain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Whiteboard implements Drawable {
    private final int id;
    private final String name;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final ArrayList<String> usernames;
    private final ArrayList<Drawable> components;

    /**
     * 
     * @param id
     *            The integer id of the whiteboard.
     * @param name
     *            The String name of the whiteboard.
     */
    public Whiteboard(int id, String name) {
        this.id = id;
        this.name = name;
        this.usernames = new ArrayList<String>();
        this.components = new ArrayList<Drawable>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * The whiteboard draws each of its components in turn.
     * 
     * @param graphics
     *            The Graphics 2D object that represents the whiteboard.
     * 
     */
    @Override
    public void drawTo(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        for (Drawable d : this.components) {
            d.drawTo(graphics);
        }
    }

    /**
     * Adds a Drawable to the whiteboard's list of components.
     * 
     * @param d
     *            The Drawable object that needs to be added to the whiteboard.
     */
    public void addDrawable(Drawable d) {
        components.add(d);
    }

    /**
     * 
     * @param username
     *            Name of user who wants to be added to the current whiteboard.
     * 
     * 
     */
    public void signInUser(String username) {
        usernames.add(username);
    }

    /**
     * 
     * @param username
     *            Name of user who will be removed from the current whiteboard.
     * 
     */
    public void signOffUser(String username) {
        usernames.remove(username);
    }

}