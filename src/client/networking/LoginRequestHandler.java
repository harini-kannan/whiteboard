package client.networking;

/**
 * LoginRequestHandler parses and handles all incoming messages when in the
 * Login window.
 * 
 * @author hkannan
 * 
 */
public class LoginRequestHandler implements RequestHandler {

    private LoginDelegate loginDelegate;

    public LoginRequestHandler(LoginDelegate ld) {
        this.loginDelegate = ld;
    }

    /**
     * Parses input according to the following grammar. Calls relevant delegate
     * method.
     * 
     * nick_in_use := "NICKINUSE" NEWLINE nick_ok := "NICKOK" menu_list
     */
    @Override
    public void parseString(String input) {
        if (input.equals("NICKINUSE")) {
            loginDelegate.onNicknameAlreadyInUse();
        } else if (input.equals("NICKOK")) {
            loginDelegate.onNicknameSuccess();
        } else {
            throw new RuntimeException("LoginRequestHandler does not know how to handle " + input);
        }
    }
}
