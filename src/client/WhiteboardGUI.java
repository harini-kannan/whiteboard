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
 * A GUI interface that inlcudes a WhiteboardPanel and some tools that
 * change drawing settings. Also includes a list of users logged in.
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

    public WhiteboardGUI(JFrame owner, Whiteboard whiteboard, ClientSocket clientSocket) {
        super(owner, "White Board - " + whiteboard.getName(), true);  // modal
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
        this.clientSocket.sendLeave();;
        super.dispose();
    }
    
    @Override
    public void addDrawableToWhiteBoard(Drawable d) {
        this.whiteboardPanel.addDrawableToBoard(d);
    }
    
    @Override
    public void signOnUser(String username) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) this.userList.getModel();
        listModel.addElement(username);
    }
    
    @Override
    public void signOffUser(String username) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) this.userList.getModel();
        listModel.removeElement(username);
    }

}
