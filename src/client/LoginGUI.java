package client;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import client.networking.ClientSocket;
import client.networking.LoginDelegate;
import client.networking.LoginRequestHandler;

/**
 * LoginGUI is the entry-point to the Whiteboard interface. It shows a dialog
 * requesting for the user to enter a nickname and then will only show 
 * the menu interface if the nickname is valid.
 * 
 * 
 * LoginGUI Testing Strategy
 * =========================
 * 1) Test that lack of a server or a failed server connection should pop an error dialog
 * 2) Test that hitting the cancel button exits the program
 * 3) Test that typing in a nickname that already exists on the server prompts for a different nickname
 * 4) Test that typing in a unqiue nickname shows the MenuGUI
 * 
 * Thread Safety Argument: This GUI is invoked on the Swing EDT. The field ClientSocket 
 * is only used for writing messages which are added to a thread-safe queue that
 * the ClientSocket is constantly polling.
 *
 */
public class LoginGUI implements LoginDelegate {    
    private ClientSocket clientSocket;
    private String username;
    
    public LoginGUI(ClientSocket s) {
        clientSocket = s;
        this.clientSocket.switchHandler(new LoginRequestHandler(this));
    }
    
    /**
     * Requests the user to input their nickname.
     */
    public void requestLogin() {
        this.username = JOptionPane.showInputDialog("Give a nickname we can identify you with.");
        
        trySendingNickname(this.username);
    }

    @Override
    public void onNicknameAlreadyInUse() {
    	if (!SwingUtilities.isEventDispatchThread()) {
    		SwingUtilities.invokeLater(new Runnable() {
    	        @Override
    	        public void run() {
    	            onNicknameAlreadyInUse();
    	        }
    	    });
    		
    		return;
    	}
    	
        this.username = JOptionPane.showInputDialog("Sorry that nickname is taken. Give a different nickname we can identify you with.");
        
        trySendingNickname(this.username);
    }
    
    private void trySendingNickname(String nickname) {
        if (nickname == null) {
            clientSocket.sendBye();
            return;
        }
        
        this.clientSocket.sendNickname(nickname);
    }

    @Override
    public void onNicknameSuccess() {
        MenuGUI menuGUI = new MenuGUI(this.clientSocket, this.username);
        menuGUI.setVisible(true);
    }
}
