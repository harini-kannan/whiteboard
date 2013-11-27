package domain;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Whiteboard implements Drawable {
    public final int id;
    public final String name;
    
    private ArrayList<String> usernames;
    private ArrayList<Drawable> components;

    public Whiteboard(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    @Override
    public void drawTo(Graphics2D graphics) {
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
