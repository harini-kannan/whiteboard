package server.messaging;

/**
 * A specific message for the MessageBus. 
 * 
 * This will generally be used in the form of anonymous classes, e.g.
 * 
 * 		Action<ServerMenu> action = new Action<ServerMenu>() {
 * 			public void perform(ServerMenu menu) {
 * 					menu.publishToClients("hello!");
 * 			}
 * 		}
 * 		messageBus.publishToMenu(action);
 *
 */
public interface Action<T extends ActionQueue<?>> {
    public void perform(T t);
}
