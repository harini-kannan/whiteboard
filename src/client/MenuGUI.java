package client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import client.networking.ClientSocket;
import client.networking.MenuDelegate;
import client.networking.MenuRequestHandler;
import domain.Whiteboard;

/**
 * MenuGUI is the second level to the Whiteboard interface. It shows a dialog
 * with a menu of all the whiteboards the user can join and allows the user
 * to create a new whiteboard on the server.
 *
 */
public class MenuGUI extends JFrame implements MenuDelegate {
    private static final long serialVersionUID = 1L;
    
    // JAVA Swing GUI Elements
    private final GroupLayout layout;
    private final JLabel instructionsLabel;
    private final JButton newBoardButton;
    private final JButton joinBoardButton;
    private final JComboBox<WhiteboardMenuItem> menuList;
    
    private ClientSocket clientSocket;

    public MenuGUI(ClientSocket clientSocket) {        
        //JFrame Defaults
        super("Whiteboard Menu");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.clientSocket = clientSocket;
        this.clientSocket.switchHandler(new MenuRequestHandler(this));
        
        //Instantiate GUI Elements
        newBoardButton = new JButton("New Board");
        joinBoardButton = new JButton("Join Board");
        this.getRootPane().setDefaultButton(joinBoardButton);
        instructionsLabel = new JLabel("Please select a board from the list below or create a new one.");
        menuList = new JComboBox<WhiteboardMenuItem>();
        
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
    
    /**
     * Requests the server to create a new whiteboard and instantiates
     * an shows a new Whiteboard interface
     */
    public void createNewBoard() {
        String boardName = JOptionPane.showInputDialog("What would you like to name your whiteboard?");
        this.clientSocket.sendMake(boardName);
        Whiteboard toJoin = new Whiteboard(boardName);
        createAndShowWhiteboardGUI(toJoin); 
    }
    
    /**
     * Requests the socket to join a whiteboard and instantiates and shows
     * a new Whiteboard interface
     */
    public void joinBoard() {
        WhiteboardMenuItem selectedBoard = this.menuList.getItemAt(this.menuList.getSelectedIndex());
        this.clientSocket.sendJoin(selectedBoard.getID());

        Whiteboard toJoin = new Whiteboard(selectedBoard.getID(), selectedBoard.getName());
        createAndShowWhiteboardGUI(toJoin);
    }
    
    private void createAndShowWhiteboardGUI(Whiteboard toJoin) {
        WhiteboardGUI whiteboardGUI = new WhiteboardGUI(this, toJoin, this.clientSocket);
        
        this.setVisible(false);
        whiteboardGUI.setVisible(true);
        this.setVisible(true);
        
        clientSocket.switchHandler(new MenuRequestHandler(this));  // go back to handling our messages
    }

    @Override
    public void onMenu(ArrayList<WhiteboardMenuItem> menus) {
        menuList.removeAllItems();
        
        for (WhiteboardMenuItem m : menus) {
            menuList.addItem(m);
        }
    }

    @Override
    public void onNew(WhiteboardMenuItem menu) {
        menuList.addItem(menu);
    }

    @Override
    public void onBadID() {
        JOptionPane.showMessageDialog(this, "We're sorry. There was an error joining the board.");
    }
}
