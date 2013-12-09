package client.networking;

public interface LoginDelegate {
    public void onNicknameAlreadyInUse();
    public void onNicknameSuccess();
}
