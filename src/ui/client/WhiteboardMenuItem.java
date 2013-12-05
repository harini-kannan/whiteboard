package ui.client;

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

        public String toString()
        {
            return name;
        }
}
