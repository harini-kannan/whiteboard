package client;

import java.net.Socket;

import javax.swing.JOptionPane;

import client.networking.ClientSocket;
import client.networking.LoginDelegate;
import client.networking.LoginRequestHandler;

/**
 * LoginGUI is the entry-point to the Whiteboard interface. It shows a dialog
 * requesting for the user to enter a nickname and then will only show 
 * the menu interface if the nickname is valid.
 *
 */
public class LoginGUI implements LoginDelegate {
    
    private ClientSocket clientSocket;
    
    public LoginGUI(Socket s) {
        this.clientSocket = new ClientSocket(s);
        this.clientSocket.switchHandler(new LoginRequestHandler(this));
    }
    
    /**
     * Requests the user to input their nickname.
     */
    public void requestLogin() {
        String username = null;
        
        while (username == null) {
            username = JOptionPane.showInputDialog ( "Give a nickname we can identify you with." );
        }
        
        this.clientSocket.sendNickname(username);
    }

    @Override
    public void onNickInUse() {
        String username = null;
        
        while (username == null) {
            username = JOptionPane.showInputDialog ( "Sorry that nickname is taken. Give a different nickname we can identify you with." );
        }
        
        this.clientSocket.sendNickname(username);
    }

    @Override
    public void onNickOkay() {
        MenuGUI menuGUI = new MenuGUI(this.clientSocket);
        menuGUI.setVisible(true); 

    }

}
