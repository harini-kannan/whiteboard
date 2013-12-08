package domain;

import java.awt.Color;
import java.awt.Point;

public class DrawableParser {
    public DrawableParser() {
    }
    
    // request should all be lowercase, and have at least two elements
    public Drawable parse(String[] request) {
        if (request[1].equals("stroke"))
            return parseStroke(request);
        
        return null;
    }
    
    // TODO(ddoucet): this is pretty shitty looking
    private Stroke parseStroke(String[] request) {
        // drawing_action := “DRAW” stroke NEWLINE
        // stroke := “STROKE” COLOR THICKNESS POINT{2,}
        // THICKNESS := INT
        // COLOR := “WHITE” | “BLACK”
        // POINT := INT “,” INT

        if (request.length < 6)  // DRAW STROKE COLOR THICKNESS x1,y1 x2,y2
            return null;
        
        Color color = parseColor(request[2]);
        Integer thickness = tryParse(request[3]);
        
        if (color == null || thickness == null || thickness < 0)
            return null;
        
        Stroke ret = new Stroke(color, thickness);
        
        for (int i = 4; i < request.length; i++) {
            Point point = parsePoint(request[i]);
            
            if (point == null)
                return null;
            
            ret.addPoint(point);
        }
        
        return ret;
    }
    
    private Color parseColor(String string) {
        if (string.equals("black"))
            return Color.black;
        else if (string.equals("white"))
            return Color.white;
        
        return null;
    }
    
    private Point parsePoint(String string) {
        // POINT := INT “,” INT
        String[] split = string.split(",");
        
        Integer x, y;
        if (split.length != 2 ||
                (x = tryParse(split[0])) == null ||
                (y = tryParse(split[1])) == null)
            return null;
        
        if (x < 0 || y < 0)
            return null;
        
        return new Point(x, y);
    }
    
    // TODO(ddoucet): this is copied from MenuRequestHandler... can I pull this out somewhere?
    private Integer tryParse(String string) {
        try {
            return Integer.valueOf(string);
        }
        catch(NumberFormatException e) {
            return null;
        }
    }
}
