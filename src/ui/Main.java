package ui;

import javax.swing.SwingUtilities;

import ui.client.WhiteboardClient;
import domain.Whiteboard;

public class Main {

    /*
     * Main program. Make a window containing a WhiteboardPanel.
     */
    public static void main(String[] args) {
        // set up the UI (on the event-handling thread)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MenuGUI menuGUI = new MenuGUI();
                menuGUI.setVisible(true);
            }
        });
    }

}
