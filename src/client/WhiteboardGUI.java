 package client;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.GroupLayout.Group;

import client.networking.*;
import domain.*;

/**
 * A GUI interface that includes a WhiteboardPanel and some tools that
 * change drawing settings. Also includes a list of users logged in.
 * 
 * WhiteboardGUI Testing Strategy
 * ==============================
 * 1) Test that closing the WhiteboardGUI shows the MenuGUI
 * 2) Test that the WhiteboardGUI's title has the nickname of the current user
 * 
 * User List
 * 3) Test that the users currently editing the board is consistent with the list of names in the user list
 * 4) Test that having another cent join the board updates the user list
 * 5) Test that having another client leave the board updates the user list
 * 
 * Draw Settings
 * 6) For all colors, test that picking that color and then drawing, draws in that color
 * 7) Test that moving the thickness slider, affects how thick of a line is drawn
 * 
 * Drawing
 * Ensure multiple clients connected to the same board have consistent drawings (End-to-End Test)
 * 8) Test that joining a board shows all previously drawn strokes
 * 9) Test that leaving and returning to a board leaves the user where they left off
 * 10) Test that leaving and returning to another board doesn't draw to the previous boards server
 * 11) Test that starting to draw on one board and getting an update before finishing keeps the clients boards consistent
 * 12) Test that drawing out of bounds updates on both boards
 * 
 *
 */
public class WhiteboardGUI extends JDialog implements DrawingDelegate {
    private static final long serialVersionUID = 1L;
    
    private final static Color[] supportedColors = new Color [] {
        Color.black, Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.white
    };
    
    private final WhiteboardPanel whiteboardPanel;
    private final GroupLayout layout;
    private final ButtonGroup colorSelector;
    private final JSlider widthSelector;
    private final List<JRadioButton> colorButtons;
    private final JList<String> userList;
    
    private ClientSocket clientSocket;
    
    private static String getWindowTitle(String nickname, Whiteboard whiteboard) {
    	return nickname + "'s White Board - " + whiteboard.getName();
    }

    public WhiteboardGUI(JFrame owner, Whiteboard whiteboard, ClientSocket clientSocket, String nickname) {
    	super(new DummyFrame(getWindowTitle(nickname, whiteboard)), getWindowTitle(nickname, whiteboard), true);
    	
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.whiteboardPanel = new WhiteboardPanel(whiteboard, clientSocket);
        
        DrawingRequestHandler drawingRequestHandler = new DrawingRequestHandler(this);
        this.clientSocket = clientSocket;
        this.clientSocket.switchHandler(drawingRequestHandler);
        
        //Setup Group Layout
        Container cp = this.getContentPane();
        layout = new GroupLayout(cp);
        cp.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (String username : whiteboard.getUsers()) {
            listModel.addElement(username);
        }
        
        this.widthSelector = new JSlider(JSlider.VERTICAL,0,100,1);
        this.widthSelector.setMajorTickSpacing(10);
        this.widthSelector.setMinorTickSpacing(5);
        this.widthSelector.setPaintTicks(true);
        this.widthSelector.setPaintLabels(true);
        
        this.colorSelector = new ButtonGroup();
        
        colorButtons = new ArrayList<>();
        
        for (Color color : supportedColors) {
            colorButtons.add(createColorButton(color));
        }
        
        this.userList = new JList<String>(listModel);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(
                        addColorButtons(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
                        .addComponent(widthSelector)
                        .addComponent(userList))
                .addComponent(whiteboardPanel)        
                );
        
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                        addColorButtons(layout.createSequentialGroup())
                        .addComponent(widthSelector)
                        .addComponent(userList))
                .addComponent(whiteboardPanel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
            );
      
        this.pack();
        
        this.widthSelector.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    whiteboardPanel.setDrawThickness(source.getValue());
                }
            }
        });
    }
    
    private JRadioButton createColorButton(final Color color) {
        JRadioButton button = new JRadioButton();
        button.setIcon(new ColorIcon(color));
        button.setSelectedIcon(new SelectedColorIcon(color));
        button.setSelected(true);
        this.colorSelector.add(button);
        
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(color);
            }
        });
        
        return button;
    }
    
    private Group addColorButtons(Group group) {
        for (JRadioButton button : colorButtons)
            group.addComponent(button);
        return group;
    }
    
    @Override
    public void dispose() {
        this.clientSocket.sendLeave();
        super.dispose();
    }
    
    @Override
    public void onNewDrawableRecieved(Drawable d) {
        this.whiteboardPanel.addDrawableToBoard(d);
    }
    
    @Override
    public void onUserSignOn(String username) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) this.userList.getModel();
        listModel.addElement(username);
    }
    
    @Override
    public void onUserSignOff(String username) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) this.userList.getModel();
        listModel.removeElement(username);
    }

}
