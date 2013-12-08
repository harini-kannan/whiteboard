package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * This will be used to create color icons for selecting draw color
 */
public class ColorIcon implements Icon {
    private Color color;  
    
    public ColorIcon(Color color) {  
      this.color = color;  
    } 

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);  
        g.fillOval (x, y, getIconWidth(), getIconHeight()); 
    }

    @Override
    public int getIconWidth() {
        return 10;
    }

    @Override
    public int getIconHeight() {
        return 10;
    }

}
