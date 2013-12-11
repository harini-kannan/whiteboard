package domain;

import java.awt.Color;
import java.awt.Point;

/**
 * DrawableParser is a helper class whose purpose is to parse Server messages
 * into Stroke objects.
 * 
 * @author hkannan
 * 
 */
public class DrawableParser {
    public DrawableParser() {
    }

    /**
     * 
     * @param request
     *            String[] array holding each token of the server message.
     * @require Requires that request should be in all lowercase and have at
     *          least two elements.
     * @return Stroke object corresponding to server message.
     */

    public Drawable parse(String[] request) {
        if (request[1].equals("stroke"))
            return parseStroke(request);

        return null;
    }

    /**
     * Parses server message according to the following grammar:
     * 
     * drawing_action := "DRAW" stroke NEWLINE stroke := "STROKE" COLOR
     * THICKNESS POINT{2,} THICKNESS := INT COLOR := INT POINT := INT "," INT
     * 
     * 
     * @param request
     *            String[] array holding each token of the server message.
     * @require Requires that request should be in all lowercase and have at
     *          least two elements.
     * @return Stroke object corresponding to server message.
     */
    // TODO(ddoucet): this is pretty shitty looking

    private Stroke parseStroke(String[] request) {

        if (request.length < 6) // DRAW STROKE COLOR THICKNESS x1,y1 x2,y2
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

    /**
     * 
     * @param string
     *            String representing color name.
     * @return Color object associated with name.
     */
    private Color parseColor(String string) {
        Integer rgb = tryParse(string);

        if (rgb == null)
            return null;

        return new Color(rgb);
    }

    /**
     * 
     * @param string
     *            String of coordinates of the form (int, int)
     * @return Point corresponding to the coordinates.
     */
    private Point parsePoint(String string) {
        // POINT := INT “,” INT
        String[] split = string.split(",");

        Integer x, y;
        if (split.length != 2 || (x = tryParse(split[0])) == null
                || (y = tryParse(split[1])) == null)
            return null;

        if (x < 0 || y < 0)
            return null;

        return new Point(x, y);
    }

    // TODO(ddoucet): this is copied from MenuRequestHandler... can I pull this
    // out somewhere?

    /**
     * 
     * @param string
     *            Represents x-coord or y-coord
     * @return parsed Integer value of the coordinate.
     */
    private Integer tryParse(String string) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
