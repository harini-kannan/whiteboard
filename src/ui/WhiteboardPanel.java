package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import domain.Stroke;
import domain.Whiteboard;

/**
 * WhiteboardPanel represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
public class WhiteboardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    // image where the user's drawing is stored
    private Image drawingBuffer;
    private Whiteboard whiteBoard;
    private Color drawColor;
    private int drawThickness = 1;
     
    /**
     * Make a canvas.
     * @param width width in pixels
     * @param height height in pixels
     */
    public WhiteboardPanel(Whiteboard whiteBoard) {
        this.whiteBoard = whiteBoard;
        this.drawColor = Color.BLACK;
        this.setPreferredSize(new Dimension(Whiteboard.WIDTH, Whiteboard.HEIGHT));
        addDrawingController();
        // note: we can't call makeDrawingBuffer here, because it only
        // works *after* this canvas has been added to a window.  Have to
        // wait until paintComponent() is first called.
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        // If this is the first time paintComponent() is being called,
        // make our drawing buffer.
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        // Copy the drawing buffer to the screen.
        g.drawImage(drawingBuffer, 0, 0, null);
    }
    
    public void setDrawColor(Color color) {
        this.drawColor = color;
    }
    
    public void setDrawThickness(int thickness) {
        this.drawThickness = thickness;
    }
    
    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private void makeDrawingBuffer() {
        drawingBuffer = createImage(getWidth(), getHeight());
        Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
        this.whiteBoard.drawTo(g);
    }
  
    
    /*
     * Draw a line between two points (x1, y1) and (x2, y2), specified in
     * pixels relative to the upper-left corner of the drawing buffer.
     */
    private void drawLineSegment(int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
        
        g.setColor(this.drawColor);
        g.setStroke(new BasicStroke(this.drawThickness));
        g.drawLine(x1, y1, x2, y2);
        
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }
    
    /*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }
    
    /*
     * DrawingController handles the user's freehand drawing.
     */
    private class DrawingController implements MouseListener, MouseMotionListener {
        // store the coordinates of the last mouse event, so we can
        // draw a line segment from that last point to the point of the next mouse event.
        private int lastX, lastY; 
        private ArrayList<Point> points;

        /*
         * When mouse button is pressed down, start drawing.
         */
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
            points = new ArrayList<Point>();
            points.add(new Point(lastX, lastY));
        }

        /*
         * When mouse moves while a button is pressed down,
         * draw a line segment.
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            drawLineSegment(lastX, lastY, x, y);
            points.add(new Point(x, y));
            lastX = x;
            lastY = y;
        }
        
        public void mouseReleased(MouseEvent e) { 
            Stroke newStroke = new Stroke(drawColor, drawThickness);
            for (Point p : points) {
                newStroke.addPoint(p);
            }
            whiteBoard.addDrawable(newStroke);
        }

        // Ignore all these other mouse events.
        public void mouseMoved(MouseEvent e) { }
        public void mouseClicked(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }
}
