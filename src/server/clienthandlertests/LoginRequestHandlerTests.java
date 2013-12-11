package server.clienthandlertests;

import org.junit.Test;

import server.messaging.MessageBus;
import server.requesthandlers.LoginRequestHandler;
import server.requesthandlers.MenuRequestHandler;

/**
 * Test the parser for login requests
 * 
 * We're looking for two things here: 1) did it parse correctly and (if
 * necessary) send something to the message bus 2) did it transition correctly
 * into another request handler
 * 
 * Look in LoginRequestHandler for the possible things it can parse. Each is a
 * separate unit test. This testing suite tests the LoginRequestHandler.
 * 
 * @author hkannan
 * 
 */
public class LoginRequestHandlerTests {
    @Test
    public void nickOkayTest() {
        MessageBus messageBus = new MessageBus();
        TestableClientHandler clientHandler = new TestableClientHandler();

        LoginRequestHandler handler = Utilities.createLoginRequestHandler(
                messageBus, clientHandler);
        handler.handle("NICK waffles");

        assert (clientHandler.getCurrentRequestHandler() instanceof MenuRequestHandler);
    }

    private MessageBus createMessageBusWithRegisteredUser(
            String registeredNickname) {
        MessageBus messageBus = new MessageBus();

        TestableClientHandler holder = new TestableClientHandler();
        holder.setNickname("waffles");

        messageBus.subscribeClient(holder);

        return messageBus;
    }

    @Test
    public void nickInUseTest() {
        MessageBus messageBus = createMessageBusWithRegisteredUser("waffles");

        TestableClientHandler clientHandler = new TestableClientHandler();
        LoginRequestHandler handler = Utilities.createLoginRequestHandler(
                messageBus, clientHandler);

        handler.handle("NICK waffles");

        assert (clientHandler.getCurrentRequestHandler() instanceof LoginRequestHandler);
        Utilities.assertHasCorrectMessages(clientHandler.getAllMessages(),
                "NICKINUSE");
    }

    @Test
    public void specifyNickTest() {
        MessageBus messageBus = createMessageBusWithRegisteredUser("waffles");

        TestableClientHandler clientHandler = new TestableClientHandler();
        LoginRequestHandler handler = Utilities.createLoginRequestHandler(
                messageBus, clientHandler);

        handler.handle("MAKE waffles");

        assert (clientHandler.getCurrentRequestHandler() instanceof LoginRequestHandler);
        Utilities.assertHasCorrectMessages(clientHandler.getAllMessages(),
                "SPECIFYNICK");
    }
}
