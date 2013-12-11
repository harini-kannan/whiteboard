package client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
 * MenuGUI Testing Strategy
 * ========================
 * 1) Test that the menu displays a list of boards consistant with what is on the server
 * 
 * Creating New Board
 * 2) Test that clicking NEW BOARD and then CANCEL returns the menu to focus
 * 3) Test that clicking NEW BOARD and then entering an empty string prompts the user to name the board
 * 4) Test that clicking NEW BOARD and then entering a string shows the WhiteboardGUI
 * 
 * Joining a Board
 * 5) Test that clicking JOIN BOARD when there are no boards on the server prompts the user to create a new board
 * 6) Test that clicking JOIN BOARD when a board is selected shows the WhiteboardGUI
 * 
 * Concurrency (Testing With Multiple Clients)
 * 7) Test that adding a new board in one client updates the other client with this change
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
    
    private final String nickname;
    private final ClientSocket clientSocket;

    public MenuGUI(final ClientSocket clientSocket, String nickname) {        
        //JFrame Defaults
        super("Whiteboard Menu");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.nickname = nickname;
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
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		try {
              		clientSocket.sendBye();
              	}
          		catch (Exception ex) {
          			System.out.println("Exception while trying to send bye");
          			ex.printStackTrace();
          		}
        		  
        		dispose();
        	  }
        	});
        
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
        if (boardName == null) {
            return;
        }
        else if (boardName.equals("")) {
            JOptionPane.showMessageDialog(null, "The name you entered is not valid.");
            return;
        }
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
        if (selectedBoard == null) {
            JOptionPane.showMessageDialog(this, "Please create a new board to join.");
            return;
        }
        this.clientSocket.sendJoin(selectedBoard.getID());

        Whiteboard toJoin = new Whiteboard(selectedBoard.getID(), selectedBoard.getName());
        createAndShowWhiteboardGUI(toJoin);
    }
    
    private void createAndShowWhiteboardGUI(Whiteboard toJoin) {
        WhiteboardGUI whiteboardGUI = new WhiteboardGUI(this, toJoin, this.clientSocket, this.nickname);
        
        this.setVisible(false);
        whiteboardGUI.setVisible(true);
        this.setVisible(true);
        
        clientSocket.switchHandler(new MenuRequestHandler(this));  // go back to handling our messages
    }

    @Override
    public void onMenuResponse(ArrayList<WhiteboardMenuItem> menus) {
        menuList.removeAllItems();
        
        for (WhiteboardMenuItem m : menus) {
            menuList.addItem(m);
        }
    }

    @Override
    public void onNewMenuItemRecieved(WhiteboardMenuItem menu) {
        menuList.addItem(menu);
    }

    @Override
    public void onInvalidBoardIDRequest() {
        JOptionPane.showMessageDialog(this, "We're sorry. There was an error joining the board.");
    }
}
