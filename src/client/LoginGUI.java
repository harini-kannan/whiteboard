package client;

import java.net.Socket;

import javax.swing.JOptionPane;

import client.networking.ClientSocket;
import client.networking.LoginDelegate;
import client.networking.LoginRequestHandler;

public class LoginGUI implements LoginDelegate {
    
    private ClientSocket clientSocket;
    
    public LoginGUI(Socket s) {
        this.clientSocket = new ClientSocket(s);
        this.clientSocket.switchHandler(new LoginRequestHandler(this));
    }
    
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
