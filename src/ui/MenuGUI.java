package ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import domain.Whiteboard;
import ui.client.WhiteboardClient;
import ui.client.WhiteboardMenuItem;

public class MenuGUI extends JFrame implements WindowFocusListener {
    private static final long serialVersionUID = 1L;
    
    // JAVA Swing GUI Elements
    private final GroupLayout layout;
    private final JLabel instructionsLabel;
    private final JButton newBoardButton;
    private final JButton joinBoardButton;
    private final JComboBox<WhiteboardMenuItem> menuList;
    
    private WhiteboardClient whiteboardClient;

    public MenuGUI() {        
        //JFrame Defaults
        super("Whiteboard Menu");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowFocusListener(this);
        
        this.whiteboardClient = new WhiteboardClient();
        
        boolean validUsername = false;
        
        while (!validUsername) {
            String username = JOptionPane.showInputDialog ( "Give a nickname we can identify you with." );
            if (username == null) { // User clicked 'Cancel'
                continue;
            }
            validUsername = this.whiteboardClient.loginUser(username);
        }
        
        ArrayList<WhiteboardMenuItem> serverBoards = this.whiteboardClient.getMenu();
        
        if (serverBoards.size() == 0) {
            createNewBoard();
        }

        //Instantiate GUI Elements
        newBoardButton = new JButton("New Board");
        joinBoardButton = new JButton("Join Board");
        this.getRootPane().setDefaultButton(joinBoardButton);
        instructionsLabel = new JLabel("Please select a board from the list below or create a new one.");
        WhiteboardMenuItem[] toPassIn = {};
        menuList = new JComboBox<WhiteboardMenuItem>(serverBoards.toArray(toPassIn));
        
        //Setup Group Layout
        Container cp = this.getContentPane();
        layout = new GroupLayout(cp);
        cp.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(instructionsLabel) 
                .addComponent(menuList) 
                .addGroup(layout.createSequentialGroup()
                        .addComponent(newBoardButton)
                        .addComponent(joinBoardButton))
                );
        
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(instructionsLabel) 
                .addComponent(menuList)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(newBoardButton)
                        .addComponent(joinBoardButton))      
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
            );
        
        this.pack();
        
        //Add ActionListeners to GUI Elements
        newBoardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewBoard();
            }
        });
        
        joinBoardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                joinBoard();
            }
        });
                
    }
    
    public void createNewBoard() {
        String boardName = JOptionPane.showInputDialog("What would you like to name your whiteboard?");
        Whiteboard toJoin = this.whiteboardClient.joinBoard(this.whiteboardClient.createBoard(boardName));
        WhiteboardGUI whiteboardGUI = new WhiteboardGUI(toJoin, this.whiteboardClient);
        whiteboardGUI.setVisible(true); 
    }
    
    public void joinBoard() {
        WhiteboardMenuItem selectedBoard = this.menuList.getItemAt(this.menuList.getSelectedIndex());
        Whiteboard toJoin = this.whiteboardClient.joinBoard(selectedBoard.getID());
        WhiteboardGUI whiteboardGUI = new WhiteboardGUI(toJoin, this.whiteboardClient);
        whiteboardGUI.setVisible(true); 
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        this.menuList.removeAllItems();
        for (WhiteboardMenuItem m : this.whiteboardClient.getMenu()) {
            this.menuList.addItem(m);
        }
    }

    @Override
    public void windowLostFocus(WindowEvent e) { }
}
