package domain;

import java.awt.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class StrokeTests {
    private Stroke createStroke(Color color, Integer thickness, Point... points) {
        Stroke ret = new Stroke(color, thickness);
        
        for (Point point : points)
            ret.addPoint(point);
        
        return ret;
    }
    
    private String[] createRequest(String request) {
        return request.toLowerCase().split(" ");
    }
    
    private void assertRequestEquals(String request, Stroke stroke) {
        DrawableParser parser = new DrawableParser();
        
        assertEquals(stroke, parser.parse(createRequest(request)));
        assertEquals(request, stroke.encode().toLowerCase());
    }
    
    private void assertRequestFails(String request) {
        DrawableParser parser = new DrawableParser();
        
        assertNull(parser.parse(createRequest(request)));
    }
    
    @Test
    public void decodeBlackTwoPointsTest() {
        assertRequestEquals(
            "draw stroke 0 1 1,1 20,20", 
            createStroke(Color.black, 1, new Point(1, 1), new Point(20, 20)));
    }
    
    @Test
    public void decodeWhiteTwoPointsTest() {
        assertRequestEquals(
            "draw stroke 16777215 1 1,1 20,20",
            createStroke(Color.white, 1, new Point(1, 1), new Point(20, 20)));
    }
    
    @Test
    public void decodeMultiplePointsTest() {
        assertRequestEquals(
            "draw stroke 16777215 1 1,1 20,20 14,14 15,15 16,16 17,17 18,18",
            createStroke(Color.white, 1, 
                new Point(1, 1), new Point(20, 20), new Point(14, 14),
                new Point(15, 15), new Point(16, 16), new Point(17, 17), new Point(18, 18)));
    }
    
    @Test
    public void decodeLargeThicknessBlackTwoPointsTest() {
        assertRequestEquals(
            "draw stroke 16777215 100 1,1 20,20",
            createStroke(Color.white, 100, new Point(1, 1), new Point(20, 20)));
    }
    
    @Test
    public void decodeNegativeThicknessFailsTest() {
        assertRequestFails("draw stroke 16777215 -100 1,1 20,20");
    }
    
    @Test
    public void decodeOnePointFailsTest() {
        assertRequestFails("draw stroke 16777215 1 1,1");
    }
    
    @Test
    public void decodeNoThicknessFailsTest() {
        assertRequestFails("draw stroke 16777215 1,1 20,20");
    }
    
    @Test
    public void decodeNoColorFailsTest() {
        assertRequestFails("draw stroke 1 1,1 20,20");
    }
    
    @Test
    public void decodeBadColorFailsTest() {
        assertRequestFails("draw stroke aquamarine 1 1,1 20,20");
    }
    
    @Test
    public void decodeFloatingPointThicknessFailsTest() {
        assertRequestFails("draw stroke 16777215 1.0 1,1 20,20");
    }
    
    @Test
    public void decodePointMissingParamFailsTest() {
        assertRequestFails("draw stroke 16777215 1 1, 20,20");
        assertRequestFails("draw stroke 16777215 1 ,1 20,20");
    }
    
    @Test
    public void decodeNegativePointParamFailsTest() {
        assertRequestFails("draw stroke 16777215 -1,1 20,20");
    }
}
