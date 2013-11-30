package domain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Whiteboard implements Drawable {
    public final int id;
    public final String name;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private ArrayList<String> usernames;
    private ArrayList<Drawable> components;

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
     * @return True if user was successfully added. False if not.
     */
    public boolean signInUser(String username) {
        return usernames.add(username);
    }

    /**
     * 
     * @param username
     *            Name of user who will be removed from the current whiteboard.
     * @return True if user was successfully removed. False if not.
     */
    public boolean signOffUser(String username) {
        return usernames.remove(username);
    }

}
