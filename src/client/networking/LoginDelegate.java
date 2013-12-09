package client.networking;

/**
 * LoginDelegate is an interface whose methods will be implemented in the GUI.
 * 
 * @author hkannan
 * 
 */
public interface LoginDelegate {
    public void onNicknameAlreadyInUse();
    public void onNicknameSuccess();
}
