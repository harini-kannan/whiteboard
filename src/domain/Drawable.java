package domain;

import java.awt.Graphics2D;

/** Each Stroke implements the Drawable interface. **/

public interface Drawable {

    /**
     * Uses graphics object to draw the stroke in the whiteboard window.
     * 
     * @param graphics
     */
    public void drawTo(Graphics2D graphics);

    /**
     * 
     * @return String representation of Drawable.
     */
    public String encode();
}
