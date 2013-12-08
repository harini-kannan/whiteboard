package client;

/**
 * WhiteboardMenuItem is a data encapsulation object for listing whiteboards
 * that can be used as the type argument for Swing objects such as JList
 *
 */
public class WhiteboardMenuItem {
        private int id;
        private String name;

        public WhiteboardMenuItem(int id, String name)
        {
            this.id = id;
            this.name = name;
        }

        public int getID()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public String toString()
        {
            return name;
        }
}
