package domain;

import java.awt.Graphics2D;

public interface Drawable {
    public void drawTo(Graphics2D graphics);
    public String encode();
}
