package domain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Stroke implements Drawable {
    private Color strokeColor;
    private int strokeThickness;
    private ArrayList<Point> points;
    
    public Stroke() {
        this.strokeColor = Color.BLACK;
        this.strokeThickness = 1;
    }
    
    public Stroke(Color strokeColor, int strokeThickness) {
        this.strokeColor = strokeColor;
        this.strokeThickness = strokeThickness;
        this.points = new ArrayList<Point>();
    }

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
    
    public boolean addPoint(int x, int y) {
        return points.add(new Point(x,y));
    }

}
