package domain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Stroke implements Drawable {
    private Color strokeColor;
    private Integer strokeThickness;
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
        return equals((Stroke)rhs);
    }
    
    public boolean equals(Stroke rhs) {
        return strokeColor.equals(rhs.strokeColor) &&
            strokeThickness.equals(rhs.strokeThickness) &&
            points.equals(rhs.points);
    }
    
    @Override
    public int hashCode() {
        return strokeColor.hashCode() * 1109 * 1109 +
            strokeThickness.hashCode() * 1109 +
            points.hashCode();
    }

    /**
     * Draws the set of lines naturally defined by the Stroke's set of points
     * with the specified color and thickness.
     */
    @Override
    public void drawTo(Graphics2D graphics) {
        graphics.setColor(this.strokeColor);
        graphics.setStroke(new BasicStroke(this.strokeThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
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

    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder("DRAW STROKE ")
            .append(encodeColor())
            .append(" ")
            .append(strokeThickness);
        
        for (Point point : points)
            builder
                .append(" ")
                .append(encodePoint(point));
        
        return builder.toString();
    }
    
    private String encodeColor() {
        return Integer.toString(strokeColor.getRGB());
    }
    
    private String encodePoint(Point point) {
        return String.format("%d,%d", point.x, point.y);
    }
}
