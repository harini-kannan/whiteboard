package client;

import javax.swing.JFrame;

/** 
 * See http://stackoverflow.com/questions/8006502/show-jdialog-on-taskbar
 * This is a workaround for certain Linux machines.
 */
class DummyFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	DummyFrame(String title) {
        super(title);
        setUndecorated(true);
        setVisible(true);
        setLocationRelativeTo(null);
    }
}