package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * This will be used to create selectedcolor icons for selecting draw color
 */
public class SelectedColorIcon extends ColorIcon {
    
    public SelectedColorIcon(Color color) {  
       super(color);
    } 

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintIcon(c, g, x, y);
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(5));
        g2.drawRect(x, y, this.getIconWidth(), this.getIconHeight());
        g2.setStroke(oldStroke);
    }

}
