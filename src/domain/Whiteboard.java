package domain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Whiteboard implements Drawable {
    private final int id;
    private final String name;
    
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    
    private ArrayList<String> usernames;
    private ArrayList<Drawable> components;

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
    
    @Override
    public void drawTo(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,  0,  WIDTH, HEIGHT);
        for (Drawable d : this.components) {
            d.drawTo(graphics);
        }
    }
    
    public void addDrawable(Drawable d) {
        components.add(d);
    }
    
    public boolean signInUser(String username) {
        return usernames.add(username);
    }
    
    public boolean signOffUser(String username) {
        return usernames.remove(username);
    }

}
