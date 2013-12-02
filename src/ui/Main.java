package ui;

import javax.swing.SwingUtilities;

import domain.Whiteboard;

public class Main {

    /*
     * Main program. Make a window containing a WhiteboardPanel.
     */
    public static void main(String[] args) {
        // set up the UI (on the event-handling thread)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Whiteboard whiteboard = new Whiteboard(1,"Test White Board");
                WhiteboardGUI whiteboardGUI = new WhiteboardGUI(whiteboard);
                whiteboardGUI.setVisible(true);
            }
        });
    }

}
