package client.networking;

public class LoginRequestHandler implements RequestHandler {

    private LoginDelegate loginDelegate;

    public LoginRequestHandler(LoginDelegate ld) {
        this.loginDelegate = ld;
    }

    @Override
    public void parseString(String input) {
        if (input.equals("NICKINUSE")) {
            loginDelegate.onNickInUse();
        } else if (input.equals("NICKOK")) {
            loginDelegate.onNickOkay();
        } else {
            // TODO: write an appropriate error message
        }
    }
}
