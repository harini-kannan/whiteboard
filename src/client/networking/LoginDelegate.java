package client.networking;

/**
 * LoginDelegate is an interface whose callback methods will be implemented in the LoginGUI.
 * 
 */
public interface LoginDelegate {
    
    /**
     * Called by the LoginRequestHandler when a response is received
     * from the server that the nickname requested is in use
     */
    public void onNicknameAlreadyInUse();
    
    /**
     * Called by the LoginRequestHandler when a response is received
     * from the server that the nickname requested successfully
     */
    public void onNicknameSuccess();
}
