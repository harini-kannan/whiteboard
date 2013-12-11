package client;

import javax.swing.JOptionPane;

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
 */
public class LoginGUI implements LoginDelegate {
    public enum State {
        Waiting,
        ShouldExit,
        Done
    }
    
    private ClientSocket clientSocket;
    private State state;
    private String username;
    
    public LoginGUI(ClientSocket s) {
        state = State.Waiting;
        
        clientSocket = s;
        this.clientSocket.switchHandler(new LoginRequestHandler(this));
    }
    
    public State getState() {
        return state;
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
        System.out.println("Nick in use");
        this.username = JOptionPane.showInputDialog("Sorry that nickname is taken. Give a different nickname we can identify you with.");
        
        trySendingNickname(this.username);
    }
    
    private void trySendingNickname(String nickname) {
        if (nickname == null) {
            state = State.ShouldExit;
            return;
        }
        System.out.println("Not exiting");
        
        this.clientSocket.sendNickname(nickname);
    }

    @Override
    public void onNicknameSuccess() {
        state = State.Done;
        System.out.println("Done");
        MenuGUI menuGUI = new MenuGUI(this.clientSocket, this.username);
        menuGUI.setVisible(true);
    }
}
