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
     * nick_in_use := ÒNICKINUSEÓ NEWLINE nick_ok := ÒNICKOKÓ menu_list
     */
    @Override
    public void parseString(String input) {
        if (input.equals("NICKINUSE")) {
            System.out.println("nick in use");
            loginDelegate.onNicknameAlreadyInUse();
        } else if (input.equals("NICKOK")) {
            System.out.println("nick ok");
            loginDelegate.onNicknameSuccess();
        } else {
            // TODO: write an appropriate error message
        }
    }
}
