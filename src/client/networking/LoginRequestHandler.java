package client.networking;

public class LoginRequestHandler implements RequestHandler {

    private LoginDelegate loginDelegate;

    public LoginRequestHandler(LoginDelegate ld) {
        this.loginDelegate = ld;
    }

    @Override
    public void parseString(String input) {
        if (input.equals("NICKINUSE")) {
            System.out.println("nick in use");
            loginDelegate.onNickInUse();
        } else if (input.equals("NICKOK")) {
            System.out.println("nick ok");
            loginDelegate.onNickOkay();
        } else {
            // TODO: write an appropriate error message
        }
    }
}
