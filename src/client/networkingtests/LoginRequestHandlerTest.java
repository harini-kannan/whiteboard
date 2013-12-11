package client.networkingtests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import client.networking.LoginDelegate;
import client.networking.LoginRequestHandler;

/**
 * Testing strategy: parseString() is tested for the following two cases:
 * parsing a server message beginning with "NICKINUSE" (for a nickname already
 * in use), and "NICKOK" (when the user types in a nickname that is valid).
 **/

public class LoginRequestHandlerTest {

    @Test
    public void onNicknameAlreadyInUseTest() {
        LoginDelegate delegate = new LoginDelegate() {
            public boolean enteredOnNICKINUSE = false;

            @Override
            public void onNicknameAlreadyInUse() {
                enteredOnNICKINUSE = true;
            };

            @Override
            public void onNicknameSuccess() {
                throw new RuntimeException("Should not enter onNicknameSuccess");
            };

            @Override
            public String toString() {
                assertTrue(enteredOnNICKINUSE);
                return "";
            }
        };

        LoginRequestHandler handler = new LoginRequestHandler(delegate);

        String input = "NICKINUSE";
        handler.parseString(input);

        delegate.toString();
    }

    @Test
    public void onNicknameSuccessTest() {
        LoginDelegate delegate = new LoginDelegate() {
            public boolean enteredOnNicknameSucess = false;

            @Override
            public void onNicknameAlreadyInUse() {
                throw new RuntimeException(
                        "Should not enter onNicknameAlreadyInUse");
            };

            @Override
            public void onNicknameSuccess() {
                enteredOnNicknameSucess = true;
            };

            @Override
            public String toString() {
                assertTrue(enteredOnNicknameSucess);
                return "";
            }
        };

        LoginRequestHandler handler = new LoginRequestHandler(delegate);

        String input = "NICKOK";
        handler.parseString(input);

        delegate.toString();
    }
}
