package domain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Stroke implements Drawable {
    private Color strokeColor;
    private int strokeThickness;
    private ArrayList<Point> points;

    /**
     * Default constructor that sets color to black and thickness to 1.
     */
    public Stroke() {
        this.strokeColor = Color.BLACK;
        this.strokeThickness = 1;
    }

    /**
     * 
     * @param strokeColor
     *            Color object representing the desired color.
     * @param strokeThickness
     *            integer representing the desired thickness.
     */
    public Stroke(Color strokeColor, int strokeThickness) {
        this.strokeColor = strokeColor;
        this.strokeThickness = strokeThickness;
        this.points = new ArrayList<Point>();
    }

    /**
     * Draws the set of lines naturally defined by the Stroke's set of points
     * with the specified color and thickness.
     */
    @Override
    public void drawTo(Graphics2D graphics) {
        for (int i = 0; i < points.size() - 1; ++i) {
            Point pointFrom = this.points.get(i);
            Point pointTo = this.points.get(i + 1);
            graphics.setColor(this.strokeColor);
            graphics.setStroke(new BasicStroke(this.strokeThickness));
            graphics.drawLine(pointFrom.x, pointFrom.y, pointTo.x, pointTo.y);
        }
    }

    /**
     * 
     * @param x
     *            The x-coordinate of the point to add.
     * @param y
     *            The y-coordinate of the point to add.
     * @return True if the point was able to be added, False if not.
     */
    public boolean addPoint(int x, int y) {
        return points.add(new Point(x, y));
    }

}
