package domain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.*;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public List<Drawable> getDrawables() {
        return new ArrayList<>(components);
    }

    /**
     * The whiteboard draws each of its components in turn.
     * 
     * @param graphics
     *            The Graphics 2D object that the whiteboard is being drawn onto.
     * 
     */
    public void drawTo(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        for (Drawable d : this.components) {
            d.drawTo(graphics);
        }
    }

    public void addDrawable(Drawable d) {
        components.add(d);
    }

    /**
     * @param username
     *            Name of user who wants to be added to the current whiteboard.
     */
    public void signInUser(String username) {
        usernames.add(username);
    }

    /**
     * @param username
     *            Name of user who will be removed from the current whiteboard.
     */
    public void signOffUser(String username) {
        usernames.remove(username);
    }

}
