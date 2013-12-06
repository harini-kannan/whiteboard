 package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JRadioButton;

import ui.client.WhiteboardClient;
import ui.client.WhiteboardClientDelegate;
import domain.Drawable;
import domain.Whiteboard;

public class WhiteboardGUI extends JFrame implements WhiteboardClientDelegate {
    private static final long serialVersionUID = 1L;
    
    private final WhiteboardPanel whiteboardPanel;
    private final GroupLayout layout;
    private final ButtonGroup colorSelector;
    private final JRadioButton blackButton, whiteButton;
    private final JList<String> userList;
    
    private WhiteboardClient whiteboardClient;

    public WhiteboardGUI(Whiteboard whiteboard, WhiteboardClient whiteboardClient) {
        super(whiteboard.getName());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.whiteboardPanel = new WhiteboardPanel(whiteboard);
        
        this.whiteboardClient = whiteboardClient;
        this.whiteboardClient.setDelegate(this);
        
        //Setup Group Layout
        Container cp = this.getContentPane();
        layout = new GroupLayout(cp);
        cp.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        
        this.colorSelector = new ButtonGroup();
        this.blackButton = new JRadioButton("Draw");
        this.blackButton.setSelected(true);
        this.colorSelector.add(this.blackButton);
        this.whiteButton = new JRadioButton("Erase");
        this.colorSelector.add(this.whiteButton);
        this.userList = new JList<String>(listModel);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(blackButton) 
                        .addComponent(whiteButton)
                        .addComponent(userList))
                .addComponent(whiteboardPanel)        
                );
        
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(blackButton)
                        .addComponent(whiteButton)
                        .addComponent(userList))
                .addComponent(whiteboardPanel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
            );
      
        this.pack();
        
        this.blackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.BLACK);
                whiteboardPanel.setDrawThickness(1);
            }
        });
        
        this.whiteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawColor(Color.WHITE);
                whiteboardPanel.setDrawThickness(30);
            }
        });
    }
    
    @Override
    public void dispose() {
        this.whiteboardClient.leaveBoard();
        super.dispose();
    }
    
    public void addDrawableToWhiteBoard(Drawable d) {
        this.whiteboardPanel.addDrawableToBoard(d);
    }
    
    public void signOnUser(String username) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) this.userList.getModel();
        listModel.addElement(username);
    }
    
    public void signOffUser(String username) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) this.userList.getModel();
        listModel.removeElement(username);
    }

}
