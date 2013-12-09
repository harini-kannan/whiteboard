package domain;

import org.junit.Test;

public class DrawableParserTest {

    @Test
    public void parseTest() {
        DrawableParser parser = new DrawableParser();
        String input = "draw stroke black 5 5 , 7";
        String[] parsed = input.split(" ");
        Drawable stroke = parser.parse(parsed);
        System.out.println(stroke.encode());
    }
}
