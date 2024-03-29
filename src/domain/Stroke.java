package domain;

/**
 * Stroke is a datatype that represents a user-defined stroke (from when the mouse is clicked to when the mouse is released). Stroke objects are passed to the GUI and are rendered on the drawing window. They contain information about color, thickness, and the set of points that the stroke corresponds to.
 */
/**
 * 
 * Thread-safe: This datatype is thread-safe because it is immutable after creation. All fields are private and final, and the only mutator method is addPoint. addPoint() is only called on when the Stroke object is created. This happens either when the socket thread reads and parses a server message to create a Stroke object, or when the GUI thread creates a Stroke object from the user's click. Because of this, every Stroke object is immutable after creation. 
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Stroke implements Drawable {
    private final Color strokeColor;
    private final Integer strokeThickness;
    private final ArrayList<Point> points;

    public Stroke() {
        this.strokeColor = Color.BLACK;
        this.strokeThickness = 1;
        points = new ArrayList<Point>();
    }

    public Stroke(Color strokeColor, Integer strokeThickness) {
        this.strokeColor = strokeColor;
        this.strokeThickness = strokeThickness;
        this.points = new ArrayList<Point>();
    }

    @Override
    public boolean equals(Object rhs) {
        if (!(rhs instanceof Stroke))
            return false;
        return equals((Stroke) rhs);
    }

    public boolean equals(Stroke rhs) {
        return strokeColor.equals(rhs.strokeColor)
                && strokeThickness.equals(rhs.strokeThickness)
                && points.equals(rhs.points);
    }

    @Override
    public int hashCode() {
        return strokeColor.hashCode() * 1109 * 1109
                + strokeThickness.hashCode() * 1109 + points.hashCode();
    }

    /**
     * Draws the set of lines naturally defined by the Stroke's set of points
     * with the specified color and thickness.
     */
    @Override
    public void drawTo(Graphics2D graphics) {
        graphics.setColor(this.strokeColor);
        graphics.setStroke(new BasicStroke(this.strokeThickness,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 0; i < points.size() - 1; ++i) {
            Point pointFrom = this.points.get(i);
            Point pointTo = this.points.get(i + 1);
            graphics.drawLine(pointFrom.x, pointFrom.y, pointTo.x, pointTo.y);
        }
    }

    public void addPoint(int x, int y) {
        addPoint(new Point(x, y));
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    /**
     * Returns String representation of the Stroke.
     */
    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder("DRAW STROKE ")
                .append(encodeColor()).append(" ").append(strokeThickness);

        for (Point point : points)
            builder.append(" ").append(encodePoint(point));

        return builder.toString();
    }

    /**
     * 
     * @return String representation of the color.
     */
    private String encodeColor() {
        return Integer.toString(strokeColor.getRGB() & 0xffffff); // ignore
                                                                  // alpha bits
    }

    /**
     * 
     * @param point
     * @return String representation of the color.
     */
    private String encodePoint(Point point) {
        return String.format("%d,%d", point.x, point.y);
    }
}
